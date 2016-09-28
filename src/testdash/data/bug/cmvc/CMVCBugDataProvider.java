package testdash.data.bug.cmvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import testdash.data.bug.BugDataProvider;
import testdash.exception.TestDashException;
import testdash.model.BugRecord;
import testdash.model.TeamContext;

import com.ibm.sdwb.cmvc.client.api.Authentication;
import com.ibm.sdwb.cmvc.client.api.ClientDefaults;
import com.ibm.sdwb.cmvc.client.api.Command;
import com.ibm.sdwb.cmvc.client.api.CommandFactory;
import com.ibm.sdwb.cmvc.client.api.CommandResults;
import com.ibm.sdwb.cmvc.client.api.FamilyInfo;
import com.ibm.sdwb.cmvc.client.api.PasswordAuthentication;
import com.ibm.sdwb.cmvc.client.api.SessionData;

public class CMVCBugDataProvider implements BugDataProvider {

	public List getBugList(TeamContext tc,String id) {
		String cmvcHostname = tc.getProperty("cmvcHostname");
		String cmvcPortStr = tc.getProperty("cmvcPort");
		int cmvcPort = 1413;
		try {
			if (cmvcPortStr != null && cmvcPortStr.trim().length() > 0) {
				cmvcPort = Integer.parseInt(cmvcPortStr);
			}
		} catch (Throwable e) {
			throw new TestDashException("The specified CMVC port number "
					+ cmvcPortStr + " is not valid", e);
		}

		String cmvcRoot = tc.getProperty("cmvcRoot");
		String cmvcUsername = tc.getProperty("cmvcUsername");
		String cmvcPassword = tc.getProperty("cmvcPassword");
		String whereClause = tc.getProperty("whereClause");

		String resultStr = getResultStr(cmvcHostname, cmvcPort, cmvcRoot,
				cmvcUsername, cmvcPassword, whereClause);
		return constructBugList(resultStr);
	}

	private List constructBugList(String resultStr) {
		List bugList = new ArrayList();
		StringTokenizer lineSt = new StringTokenizer(resultStr, "\n");
		while (lineSt.hasMoreTokens()) {
			String line = lineSt.nextToken();
			String[] fields = line.split("\\|");
			BugRecord br = new BugRecord();
			br.setId(fields[1]);
			br.setOwner(fields[18]);

			final String datePattern = "yyyy/MM/dd hh:mm:ss";
			String creationDateStr = fields[14];
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.US);
			Date creationDate;
			try {
				creationDate = sdf.parse(creationDateStr);
			} catch (java.text.ParseException e) {
				throw new TestDashException("Cannot parse the date string - "
						+ creationDateStr + " with the pattern " + datePattern);
			}
			br.setCreationDate(creationDate);

			br.setPriority(fields[30]);
			br.setReporter(fields[22]);
			br.setSeverity("Sev" + fields[7]);
			br.setStatus(fields[5]);
			br.setSummary(fields[8]);
			bugList.add(br);
		}
		return bugList;
	}

	private String getResultStr(String cmvcHostname, int cmvcPort,
			String cmvcRoot, String cmvcUsername, String cmvcPassword,
			String whereClause) {
		ClientDefaults defaults = new ClientDefaults();
		defaults.setProperty("CMVC_ROOT", cmvcRoot);

		// construct session and authentication object
		SessionData sessionData = new SessionData();
		String sessionID = "" + (new Random()).nextInt();
		Authentication auth = new PasswordAuthentication(cmvcUsername,
				cmvcUsername, cmvcPassword, sessionID);
		sessionData.setAuthentication(auth);
		sessionData.setClientDefaults(defaults);
		// create FamilyInfo & get version from server *
		FamilyInfo familyInfo = new FamilyInfo();
		familyInfo.setHostName(cmvcHostname);
		familyInfo.setPortNumber(cmvcPort);
		try {
			familyInfo.retrieveServerVersion();
		} catch (Throwable e1) {
			throw new TestDashException(
					"Fail to retrive CMVC server version number.", e1);
		}
		// Log in
		try {
			((PasswordAuthentication) auth).login(familyInfo, sessionData);
		} catch (Throwable e1) {
			throw new TestDashException("Fail to log in CMVC server.", e1);
		}

		try {
			Command cmd = CommandFactory.getInstance().getCommand("ReportView");
			cmd.setFamilyInfo(familyInfo);
			cmd.setSessionData(sessionData);
			cmd.addObjectSpecValue("DefectView");

			cmd.addParameterValue("-where", whereClause);
			cmd.addParameterValue("-raw");
			cmd.addParameterValue("-hopCount", "1");

			CommandResults results = cmd.exec();
			String messages = results.getMessagesString();
			int rc = results.getReturnCode();
			if (rc == CommandResults.SUCCESS) {
				return results.getSuccessMessages();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				((PasswordAuthentication) sessionData.getAuthentication())
						.logout(familyInfo, sessionData);
			} catch (Throwable e) {
				throw new TestDashException("Fail to log out CMVC server.", e);
			}
		}
		return null;
	}
}
