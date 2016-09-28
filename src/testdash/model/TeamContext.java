package testdash.model;

import java.util.Properties;

public class TeamContext {
	private String fullName;

	private String erSCurveFilePath;
	private String defectSCurveFilePath;

	private Properties extraProps;
	private String erStatusProviderClassName;
	private String bugDataProviderClassName;

	public TeamContext(String fullName, String erSCurveFilePath,
			String defectSCurveFilePath, String erStatusProviderClassName,String bugDataProviderClassName) {
		super();
		this.fullName = fullName;
		this.erSCurveFilePath = erSCurveFilePath;
		this.defectSCurveFilePath = defectSCurveFilePath;
		this.erStatusProviderClassName = erStatusProviderClassName;
		this.bugDataProviderClassName = bugDataProviderClassName;

		extraProps = new Properties();
	}

	public void setExtraProperties(Properties props) {
		extraProps = props;
	}

	public void setProperty(String propName, String propValue) {
		extraProps.setProperty(propName, propValue);
	}

	public String getProperty(String propName) {
		return extraProps.getProperty(propName);
	}
	
	public String getBugDataProviderClassName() {
		return bugDataProviderClassName;
	}

	public String getErStatusProviderClassName() {
		return erStatusProviderClassName;
	}

	

	public String getFullName() {
		return fullName;
	}

	public String getErSCurveFilePath() {
		return erSCurveFilePath;
	}

	public String getDefectSCurveFilePath() {
		return defectSCurveFilePath;
	}

	
}
