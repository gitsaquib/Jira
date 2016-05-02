import java.net.URI;
import java.util.Set;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JiraUtil {

	public static JiraLoginCredentials LC = new JiraLoginCredentials(
			"http://jira.pearsoncmg.com/jira",
			"vsaqumo",
			"Pearson5"
			);
	public static void main(String[] args) {
		Issue issue = findIssueByIssueKey(LC, "PSCDEV-96034");
		IssueField foundVia = issue.getFieldByName("Found Via");
		System.out.println(foundVia.getValue());
		Set<String> labels = issue.getLabels();
		if(labels.contains("Automation")) {
			System.out.println("This is found in automation");
		} else {
			System.out.println("This is found in manual");
		}
		String description = issue.getDescription();
		if(description.toLowerCase().contains("cc") || 
				description.toLowerCase().contains("configcode") || 
				description.toLowerCase().contains("config code")||
				description.toLowerCase().contains("ccsocdct")||
				description.toLowerCase().contains("4mvreviewv2") ||
				description.toLowerCase().contains("4mvassessmts")
				) {
			System.out.println("Config Code is found in description");
		} else {
			System.out.println("Missing Config Code in description");
		}
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