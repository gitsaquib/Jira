import java.util.List;

public class IssueAttributes {
	private	boolean	foundVia;
	private	boolean	automationLabel;
	private	boolean	regressionLabelInSummary;
	private	boolean	automationLabelInSummary;
	private	String	appLabel;
	private	String	configCode;
	private	String	buildVersion;
	private	String	platformLabel;
	private	boolean	trivialPriority;
	private List<String> components;
	private List<String> fixVersion;
	private String priority;
	
	public List<String> getComponents() {
		return components;
	}
	public void setComponents(List<String> components) {
		this.components = components;
	}
	public boolean isAutomationLabel() {
		return automationLabel;
	}
	public void setAutomationLabel(boolean automationLabel) {
		this.automationLabel = automationLabel;
	}
	public boolean isRegressionLabelInSummary() {
		return regressionLabelInSummary;
	}
	public void setRegressionLabelInSummary(boolean regressionLabelInSummary) {
		this.regressionLabelInSummary = regressionLabelInSummary;
	}
	public boolean isAutomationLabelInSummary() {
		return automationLabelInSummary;
	}
	public void setAutomationLabelInSummary(boolean automationLabelInSummary) {
		this.automationLabelInSummary = automationLabelInSummary;
	}
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}
	public String getConfigCode() {
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	public String getBuildVersion() {
		return buildVersion;
	}
	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}
	public String getPlatformLabel() {
		return platformLabel;
	}
	public void setPlatformLabel(String platformLabel) {
		this.platformLabel = platformLabel;
	}
	public boolean isTrivialPriority() {
		return trivialPriority;
	}
	public void setTrivialPriority(boolean trivialPriority) {
		this.trivialPriority = trivialPriority;
	}
	public boolean isFoundVia() {
		return foundVia;
	}
	public void setFoundVia(boolean foundVia) {
		this.foundVia = foundVia;
	}
	public List<String> getFixVersion() {
		return fixVersion;
	}
	public void setFixVersion(List<String> fixVersion) {
		this.fixVersion = fixVersion;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
}
