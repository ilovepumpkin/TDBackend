package testdash.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import testdash.model.ERStatusRecord;
import testdash.model.TeamContext;

public class ERStatusManager {
	private static ERStatusManager instance;

	private ERStatusManager() {

	}

	public static ERStatusManager getInstance() {
		if (instance == null) {
			instance = new ERStatusManager();
		}
		return instance;
	}

	public String getERStatus(String format, TeamContext tc, String id) {
		String erStatusStr = "";
		ERStatusRecord record = getERStatusRecord(tc, id);
		erStatusStr = constructOutput(format, record);

		return erStatusStr;
	}

	private ERStatusRecord getERStatusRecord(TeamContext tc, String id) {
		try {
			Class clazz = Class.forName(tc.getErStatusProviderClassName());
			Method m = clazz.getMethod("getERStatusRecord", TeamContext.class,String.class);
			ERStatusRecord erStatusRecord = (ERStatusRecord) m.invoke(clazz
					.newInstance(), new Object[] { tc, id });
			return erStatusRecord;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String constructOutput(String format, ERStatusRecord record) {
		String unattempted = String.valueOf(record.getUnattemptedPoints());
		String attempted = String.valueOf(record.getAttemptedPoints());
		String blocked = String.valueOf(record.getBlockedPoints());
		String failed = String.valueOf(record.getFailedPoints());
		String complete = String.valueOf(record.getCompletedPoints());
		String permfailed = String.valueOf(record.getPermFailPoints());
		String deferred = String.valueOf(record.getDeferredPoints());

		if (format.equals("xml")) {

			StringBuffer xmlData = new StringBuffer(
					"<?xml version=\"1.0\"?><RowSet>");
			xmlData.append(constructRow("Unattempted", unattempted)).append(
					constructRow("Attempted", attempted)).append(
					constructRow("Blocked", blocked)).append(
					constructRow("Failed", failed)).append(
					constructRow("Complete", complete)).append(
					constructRow("Permfailed", permfailed)).append(
					constructRow("Deferred", deferred)).append("</RowSet>");

			return xmlData.toString();
		} else if (format.equals("csv")) {
			StringBuffer csvData = new StringBuffer(
					"Status,Unattemptted,Attemptted,Blocked,Failed,Complete,Permfailed,Deferred\n\rPoint");
			csvData.append(",").append(unattempted).append(",").append(
					attempted).append(",").append(blocked).append(",").append(
					failed).append(",").append(complete).append(",").append(
					permfailed).append(",").append(deferred);
			return csvData.toString();
		} else if (format.equals("feed")) {
			Feed feed = new Abdera().newFeed();

			feed.setId("tag:example.org,2007:/foo");
			feed.setTitle("Test Feed");
			feed.setSubtitle("Feed subtitle");
			feed.setUpdated(new Date());
			feed.addAuthor("James Snell");
			feed.addLink("http://example.com");
			feed.addLink("http://example.com/foo", "self");

			Entry entry = feed.addEntry();
			entry.setId("tag:example.org,2007:/foo/entries/1");
			entry.setTitle("Entry title");
			entry.setSummaryAsHtml("<p>This is the entry title</p>");
			entry.setUpdated(new Date());
			entry.setPublished(new Date());
			entry.addLink("http://example.com/foo/entries/1");

			StringBuffer xmlData = new StringBuffer("<root>");
			xmlData.append(constructSeries("Unattempted", unattempted)).append(
					constructSeries("Attempted", attempted)).append(
					constructSeries("Blocked", blocked)).append(
					constructSeries("Failed", failed)).append(
					constructSeries("Complete", complete)).append(
					constructSeries("Permfailed", permfailed)).append(
					constructSeries("Deferred", deferred)).append("</root>");
			entry.setContentAsXhtml(xmlData.toString());
			return feed.toString();
		}

		return null;
	}

	private String constructRow(String statusName, String pointValue) {
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<Row><StatusName>").append(statusName).append(
				"</StatusName>").append("<Point>").append(pointValue).append(
				"</Point></Row>");
		return xmlData.toString();
	}

	private String constructSeries(String statusName, String pointValue) {
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<series><title>ER Status</title><data><statusname>")
				.append(statusName).append("</statusname>").append("<point>")
				.append(pointValue).append("</point></data></series>");
		return xmlData.toString();
	}

}
