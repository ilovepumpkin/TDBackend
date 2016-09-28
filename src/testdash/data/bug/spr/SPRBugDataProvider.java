package testdash.data.bug.spr;

import java.util.ArrayList;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;

import testdash.data.bug.BugDataProvider;
import testdash.exception.TestDashException;
import testdash.model.BugRecord;
import testdash.model.TeamContext;

public class SPRBugDataProvider implements BugDataProvider {

	public List getBugList(TeamContext tc,String id) {
		String sprHost = tc.getProperty("sprHost");
		String sprPort = tc.getProperty("sprPort");
		String sprUsername = tc.getProperty("sprUsername");
		String sprPassword = tc.getProperty("sprPassword");
		String dbLocation = tc.getProperty("dbLocation");
		String query = tc.getProperty("query");

		Database db = connectDb(sprHost, sprPort, sprUsername, sprPassword,
				dbLocation);

		return constructBugList(db, query);
	}

	private List constructBugList(Database db, String query) {
		List bugList = new ArrayList();
		DocumentCollection bugs = null;
		try {
			bugs = db.search(query);
			Document bug = null;
			for (bug = bugs.getFirstDocument(); bug != null; bug = bugs
					.getNextDocument()) {
				String severity = bug.getItemValueString("Severity");
				String sprID = bug.getItemValueString("SPRID");
				String briefDesc = bug.getItemValueString("BriefDescription");
				String owner = bug.getItemValueString("Dev_Assign");
				String reporter = bug.getItemValueString("QA_Assign");
				String status = bug.getItemValueString("SPRState");
				String priority = bug.getItemValueString("Priority");

				BugRecord br = new BugRecord();
				br.setId(sprID);
				br.setSeverity(severity);
				br.setSummary(briefDesc);
				br.setOwner(owner);
				br.setReporter(reporter);
				br.setStatus(status);
				//br.setPriority(priority);
				bugList.add(br);
			}
		} catch (NotesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return bugList;
	}

	private Database connectDb(String host, String port, String username,
			String password, String dbLocation) {
		String connStr = host + ":" + port;
		Session session = null;
		Database db = null;
		try {
			session = NotesFactory.createSession(connStr, username, password);
			db = session.getDatabase(null, dbLocation);
		} catch (NotesException e) {
			throw new TestDashException(
					"Fail to establish database connection for " + dbLocation,
					e);
		}
		return db;
	}
}
