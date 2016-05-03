import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JiraUtil {

	public static JiraLoginCredentials LC = new JiraLoginCredentials( "http://jira.pearsoncmg.com/jira", "vsaqumo", "Pearson5");
	public static void main(String[] args) {
		retrieveIssueDetails("PSCDEV-96034");
	}
	
	public static IssueAttributes retrieveIssueDetails(String jiraId){
		IssueAttributes issueAttributes = new IssueAttributes();
		Issue issue = findIssueByIssueKey(LC, jiraId);
		
		IssueField foundViaStr = issue.getFieldByName("Found Via");
		boolean foundVia = null != foundViaStr? foundViaStr.equals("Automated Test") : false;
		issueAttributes.setFoundVia(foundVia);
		
		Set<String> labels = issue.getLabels();
		if(labels.contains("Automation")) {
			issueAttributes.setAutomationLabel(true);
		} else {
			issueAttributes.setAutomationLabel(false);
		}
		
		String summary = issue.getSummary();
		SimpleDateFormat formatter = new SimpleDateFormat("MMM-yyyy");
    	Date today = new Date();
    	String month_yyyy = formatter.format(today);
		if(summary.contains("[RT-"+month_yyyy+"]")) {
			issueAttributes.setRegressionLabelInSummary(true);
		} else {
			issueAttributes.setRegressionLabelInSummary(false);
		}
		
		if(summary.contains("[Automation]")) {
			issueAttributes.setAutomationLabelInSummary(true);
		} else {
			issueAttributes.setAutomationLabelInSummary(false);
		}
		
		String appLabel = "";
		if(labels.contains("2-12_app")) {
			appLabel = "2-12_app";
		} else {
			appLabel = "K-1_app";
		}
		issueAttributes.setAppLabel(appLabel);
		
		String description = issue.getDescription();
		if(description.toLowerCase().contains("cc") || 
				description.toLowerCase().contains("configcode") || 
				description.toLowerCase().contains("config code")||
				description.toLowerCase().contains("ccsocdct")||
				description.toLowerCase().contains("4mvreviewv2") ||
				description.toLowerCase().contains("4mvassessmts")
				) {
			if(description.toLowerCase().contains("ccsocdct")) {
				
			} else if(description.toLowerCase().contains("ccsocdct")) {
				
			} else if(description.toLowerCase().contains("ccsocdct")) {
				
			}
		} else {
			System.out.println("Missing Config Code in description");
		}
		return issueAttributes;
	}

	public static Issue findIssueByIssueKey(JiraLoginCredentials lc, String issueKey) {
		JiraRestClient jiraRestClient = createJiraRestClient(lc);
		SearchRestClient searchClient = jiraRestClient.getSearchClient();
		String jql = "issuekey = \"" + issueKey + "\"";
		SearchResult results = searchClient.searchJql(jql).claim();
		destroyJiraRestClient(jiraRestClient);
    	return results.getIssues().iterator().next();
	}
	
	private static JiraRestClient createJiraRestClient(JiraLoginCredentials lc) {
		final JiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();
		return jiraRestClientFactory
				.createWithBasicHttpAuthentication(URI.create(lc.getUrl()),
						lc.getUsername(), lc.getPassword());
	}

	private static void destroyJiraRestClient(JiraRestClient jiraRestClient) {
		try {
			jiraRestClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class JiraLoginCredentials {
		private String url;
		private String username;
		private String password;
		public JiraLoginCredentials(String url, String username, String password) {
			super();
			this.url = url;
			this.username = username;
			this.password = password;
		}
		public String getUrl() {
			return url;
		}
		public String getUsername() {
			return username;
		}
		public String getPassword() {
			return password;
		}
	}
}