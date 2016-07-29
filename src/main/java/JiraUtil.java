import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicPriority;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JiraUtil {

	public static JiraLoginCredentials LC = new JiraLoginCredentials( "http://jira.pearsoncmg.com/jira", "vsaqumo", "Pearson6");
	
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
				issueAttributes.setConfigCode("ccsocdct");
			} else if(description.toLowerCase().contains("4mvreviewv2")) {
				issueAttributes.setConfigCode("4mvreviewv2");
			} else if(description.toLowerCase().contains("4mvassessmts")) {
				issueAttributes.setConfigCode("4mvassessmts");
			} else if(description.toLowerCase().contains("prsnuat01")) {
				issueAttributes.setConfigCode("prsnuat01");
			} else if(description.toLowerCase().contains("prsnqa01")) {
				issueAttributes.setConfigCode("prsnqa01");
			} else if(description.toLowerCase().contains("prsnqa02")) {
				issueAttributes.setConfigCode("prsnqa02");
			}
		}
		
		if(description.toLowerCase().contains("1.6")) {
			//issueAttributes.setBuildVersion(description.substring(description.indexOf("1.6"), description.indexOf("1.6")+10));
		}
		
		if(labels.contains("iOS") || labels.contains("iOS9") || labels.contains("Windows") || labels.contains("Windows_10")) {
			if(labels.contains("iOS")) {
				issueAttributes.setPlatformLabel("iOS");
			} else if(labels.contains("iOS9")) {
				issueAttributes.setPlatformLabel("iOS9");
			} else if(labels.contains("Windows_10")) {
				issueAttributes.setPlatformLabel("Windows_10");
			} else if(labels.contains("Windows")) {
				issueAttributes.setPlatformLabel("Windows");
			}
		}
		
		BasicPriority priority = issue.getPriority();
		issueAttributes.setPriority(priority.getName());
		if(priority.getName().equalsIgnoreCase("trivial")) {
			issueAttributes.setTrivialPriority(true);
		} else {
			issueAttributes.setTrivialPriority(false);
		}
		
		List<String> components =  new ArrayList<String>();
		Iterable<BasicComponent> componentsIterable = issue.getComponents();
		if(null != componentsIterable) {
			for(BasicComponent component:componentsIterable) {
				if(component.getName().contains("PSC: App Team 1")) {
					components.add("PSC: App Team 1");
				} else if(component.getName().contains("PSC: App Team 2")) {
					components.add("PSC: App Team 2");
				} else if(component.getName().contains("PSC: Authoring")) {
					components.add("PSC: Authoring");
				} else if(component.getName().contains("PSC: Reporting")) {
					components.add("PSC: Reporting");
				} else if(component.getName().contains("PSC: RunTeam")) {
					components.add("PSC: RunTeam");
				} else if(component.getName().contains("PSC: Services")) {
					components.add("PSC: Services");
				}
			}
		}
		issueAttributes.setComponents(components);
		
		List<String> versions =  new ArrayList<String>();
		Iterable<Version> versionsIterable = issue.getFixVersions();
		if(null != versionsIterable) {
			for(Version version:versionsIterable) {
				versions.add(version.getName());
			}
		}
		issueAttributes.setFixVersion(versions);
		
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
	
	
	public static void addCommentToJira(String jiraId, String commentStr){
		addComment(LC, jiraId, commentStr);
	}
	
	public static void addComment(JiraLoginCredentials lc, String issueKey, String commentStr) {
		JiraRestClient jiraRestClient = createJiraRestClient(lc);
		IssueRestClient issueClient = jiraRestClient.getIssueClient();
		Issue issue = issueClient.getIssue(issueKey).claim();
		Comment comment = Comment.valueOf(commentStr);
		issueClient.addComment(issue.getCommentsUri(), comment);
		destroyJiraRestClient(jiraRestClient);
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