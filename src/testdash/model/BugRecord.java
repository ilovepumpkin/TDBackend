package testdash.model;

import java.util.Calendar;
import java.util.Date;

import testdash.util.XmlUtil;

public class BugRecord {
	private String id = "";
	private String status = "";
	private String severity = "";
	private String priority = "";
	private String owner = "";
	private String reporter = "";
	private Date creationDate = Calendar.getInstance().getTime();
	private String summary = "";
	private String webLink="";

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Long getAge() {
		Date today = Calendar.getInstance().getTime();
		long l = today.getTime() - creationDate.getTime();
		long d = l / (24 * 60 * 60 * 1000);
		return Long.valueOf(d);
	}

	public String toXml() {
		StringBuffer sb = new StringBuffer("<Bug>");
		sb.append("<Id>").append(id).append("</Id>");
		sb.append("<Status>").append(status).append("</Status>");
		sb.append("<Severity>").append(severity).append("</Severity>");
		sb.append("<Priority>").append(priority).append("</Priority>");
		sb.append("<Owner>").append(owner).append("</Owner>");
		sb.append("<Reporter>").append(reporter).append("</Reporter>");
		sb.append("<CreationDate>").append(creationDate).append(
				"</CreationDate>");
		sb.append("<Age>").append(getAge()).append("</Age>");
		sb.append("<Summary>").append(XmlUtil.normalize(summary)).append("</Summary>");
		sb.append("<WebLink>").append(XmlUtil.normalize(webLink)).append("</WebLink>");
		sb.append("</Bug>");
		return sb.toString();
	}
	
	public String toHTML(){
		StringBuffer sb=new StringBuffer("<table style='border:1px solid black'>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Id</td><td>").append(id).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Summary</td><td>").append(summary).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Status</td><td>").append(status).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Severity</td><td>").append(severity).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Priority</td><td>").append(priority).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Owner</td><td>").append(owner).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Reporter</td><td>").append(reporter).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Creation Date</td><td>").append(creationDate).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>Age</td><td>").append(getAge()).append("</td></tr>");
		sb.append("<tr><td style='background-color:lightgrey;font-style:bold'>WebLink</td><td>").append(webLink).append("</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}
}
