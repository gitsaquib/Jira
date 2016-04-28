package com.pearson.psc.jira;

import java.net.URI;
import java.util.Iterator;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueLink;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;

public class JiraUtil {

    private static final String JIRA_URL = "http://jira.pearsoncmg.com";
    private static final String JIRA_ADMIN_USERNAME = "vsaqumo";
    private static final String JIRA_ADMIN_PASSWORD = "Pearson5";

    public static void main(String[] args) throws Exception {
    	  
        JerseyJiraRestClientFactory f = new JerseyJiraRestClientFactory();
        JiraRestClient jc = f.createWithBasicHttpAuthentication(new URI(JIRA_URL), JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
 
        SearchResult r = jc.getSearchClient().searchJql("Status=Closed", null);
         
        Iterator<BasicIssue> it = r.getIssues().iterator();
        while (it.hasNext()) {
             
            Issue issue = jc.getIssueClient().getIssue(((BasicIssue)it.next()).getKey(), null);
             
            System.out.println("Epic: " + issue.getKey() + " " + issue.getSummary());
             
            Iterator<IssueLink> itLink = issue.getIssueLinks().iterator();
            while (itLink.hasNext()) {
                 
                IssueLink issueLink = (IssueLink)itLink.next();
                Issue issueL = jc.getIssueClient().getIssue((issueLink).getTargetIssueKey(), null);
                 
                System.out.println(issueLink.getIssueLinkType().getDescription() + ": " + issueL.getKey() + " " + issueL.getSummary());
                 
            }
             
        }
         /*
        try {
            Client client = Client.create();    
            client.addFilter(new HTTPBasicAuthFilter(JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD));
            WebResource webResource = client.resource(JIRA_URL);
            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
            String output = response.getEntity(String.class);
            System.out.println("Output from Server .... \n");
            System.out.println(output);         
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}