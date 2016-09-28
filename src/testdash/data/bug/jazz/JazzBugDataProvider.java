package testdash.data.bug.jazz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import testdash.data.bug.BugDataProvider;
import testdash.exception.TestDashException;
import testdash.model.BugRecord;
import testdash.model.TeamContext;
import testdash.util.TestDashUtil;

public class JazzBugDataProvider implements BugDataProvider {
	private static String FIELD_SEPERATOR = "\"	\"";
	private String datePattern;

	private final String SECURITY_URL="jazzSecurityURL";
	private final String QUERY_CSV_URL="jazzBugQueryCSVURL";
	private final String USER_NAME="jazzUsername";
	private final String PASSWORD="jazzPassword";
	private final String BUG_LINK_TEMPLATE="jazzBugLinkTemplate";
	private final String DATE_PATTERN="datePattern";
	
	public List getBugList(TeamContext tc,String id) {
		
		String pSecurityURL=SECURITY_URL;
		String pBugQueryCSVURL=QUERY_CSV_URL;
		String pUsername=USER_NAME;
		String pPassword=PASSWORD;
		String pBugLinkTemplate=BUG_LINK_TEMPLATE;
		String pDatePattern=DATE_PATTERN;
		
		if(id!=null){
			pSecurityURL=id+"."+SECURITY_URL;
			pBugQueryCSVURL=id+"."+QUERY_CSV_URL;
			pUsername=id+"."+USER_NAME;
			pPassword=id+"."+PASSWORD;
			pBugLinkTemplate=id+"."+BUG_LINK_TEMPLATE;
			pDatePattern=id+"."+DATE_PATTERN;
		}
		
		String jazzSecurityURL = tc.getProperty(pSecurityURL);
		if(jazzSecurityURL==null) jazzSecurityURL=tc.getProperty(SECURITY_URL);
		
		String jazzBugQueryCSVURL = tc.getProperty(pBugQueryCSVURL);
		
		String jazzUsername = tc.getProperty(pUsername);
		if(jazzUsername==null) jazzUsername=tc.getProperty(USER_NAME);
		
		String jazzPassword = tc.getProperty(pPassword);
		if(jazzPassword==null) jazzPassword=tc.getProperty(PASSWORD);
		
		String jazzBugLinkTemplate = tc.getProperty(pBugLinkTemplate);
		if(jazzBugLinkTemplate==null) jazzBugLinkTemplate=tc.getProperty(BUG_LINK_TEMPLATE);
		
		datePattern = tc.getProperty(pDatePattern);
		if(datePattern==null) datePattern=tc.getProperty(DATE_PATTERN);
		
		if(datePattern==null)
			datePattern = "MMM d, yyyy h:mm a";

		Properties authParams = new Properties();
		authParams.put("j_username", jazzUsername);
		authParams.put("j_password", jazzPassword);

		String csvData = TestDashUtil.getHTMLContentWithAuth(
				jazzBugQueryCSVURL, jazzSecurityURL, authParams);
		
		return constructBugList(csvData,jazzBugLinkTemplate);
	}

	private List constructBugList(String csvData,String jazzBugLinkTemplate) {
		StringTokenizer st = new StringTokenizer(csvData, "\n");
		// Skip the title line£Æ
		st.nextToken();

		List bugList = new ArrayList();

		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			String[] fields = line.split(FIELD_SEPERATOR);
			BugRecord br = new BugRecord();

			String id = fields[0];
			id = id.substring(1);
			br.setId(id);
			br.setStatus(fields[1]);
			br.setSeverity(fields[2]);
			br.setPriority(fields[3]);
			br.setOwner(fields[4]);
			br.setReporter(fields[5]);
			br.setWebLink(jazzBugLinkTemplate.replace("${id}", id));

			String creationDateStr = fields[6];
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern,Locale.US);
			
			// temp fix for Jazz installed on Chinese OS
			//creationDateStr=creationDateStr.substring(0,10);
						
			Date creationDate;
			try {
				creationDate = sdf.parse(creationDateStr);
			} catch (ParseException e) {
				throw new TestDashException("Cannot parse the date string - "
						+ creationDateStr + " with the pattern " + datePattern);
			}
			br.setCreationDate(creationDate);

			String summary = fields[7];
			summary = summary.substring(0, summary.length() - 1);
			br.setSummary(summary);
			bugList.add(br);
		}
		return bugList;
	}
}
