import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.atlassian.jira.rest.client.api.domain.Issue;

public class JiraApplet extends JApplet implements ActionListener {
	private JLabel inputLabel;
	private JButton doIt;
	private JTextField numVariable;
	private String jiraId;
	private Issue issue;

	public void init() {
		setBackground(Color.WHITE);
		inputLabel = new JLabel("Enter Jira Id:");
		numVariable = new JTextField(14);
		doIt = new JButton("Validate!");
		doIt.addActionListener(this);
		MyPanelClass myPanel = new MyPanelClass();
		myPanel.setName("Jira Validator");
		myPanel.add(inputLabel);
		myPanel.add(numVariable);
		myPanel.add(doIt);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		myPanel.setSize(dim.width, dim.height);
		getContentPane().add(myPanel);
	}

	public void actionPerformed(ActionEvent event) {
		jiraId = numVariable.getText();
		repaint();
	}
	
	public class MyPanelClass extends JPanel
	{
		public void paintComponent(Graphics page) {
			super.paintComponent(page);
			if(null != jiraId && !jiraId.isEmpty()) {
				numVariable.setVisible(false);
				doIt.setVisible(false);
				inputLabel.setVisible(false);
				issue = JiraUtil.findIssueByIssueKey(JiraUtil.LC, jiraId);
			}
			if(null != issue) {
				page.drawString(issue.getSummary(), 10, 10);
			}
	    }
	}
}
//test id PSCDEV-96034