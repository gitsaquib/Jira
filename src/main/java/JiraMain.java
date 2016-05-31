import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class JiraMain {

	public static void main(String[] args) {
		try {
			getJiraDefectDetails();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void getJiraDefectDetails() throws FileNotFoundException {
		File file = new File("D:\\Jira.txt");
		Scanner sc = new Scanner(new FileReader(file));
		while (sc.hasNextLine()){
			String id = sc.nextLine();
			IssueAttributes attributes = JiraUtil.retrieveIssueDetails(id);
			int i=0;
			System.out.print(id+"\t");
			for(String version:attributes.getFixVersion()) {
				if(i==0) {
					System.out.print(version);
				} else {
					System.out.print(", "+version);
				}
				i++;
			}
			System.out.println("\t"+attributes.getPriority());
		}
	}
}
