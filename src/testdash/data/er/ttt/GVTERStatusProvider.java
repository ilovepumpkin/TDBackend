package testdash.data.er.ttt;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import testdash.data.er.ERStatusProvider;
import testdash.model.ERStatusRecord;
import testdash.model.TeamContext;
import testdash.util.TestDashUtil;

public class GVTERStatusProvider implements ERStatusProvider {

	public ERStatusRecord getERStatusRecord(TeamContext tc, String id) {
		String tttViewURL = tc.getProperty("tttViewURL");
		String phaseName = tc.getProperty("phaseName");
		String authURL = tc.getProperty("authURL");
		String authUsername = tc.getProperty("authUsername");
		String authPassword = tc.getProperty("authPassword");
		String columns = tc
				.getProperty("unattempted,blocked,attempted,failed,permfailed,complete,deferred");

		int[] colIndexes = new int[7];
		StringTokenizer st = new StringTokenizer(columns, ",");
		int i = 0;
		while (st.hasMoreTokens()) {
			int colIndex = Integer.parseInt(st.nextToken());
			colIndexes[i++] = colIndex;
		}

		Properties authProps = new Properties();
		authProps.setProperty("Username", authUsername);
		authProps.setProperty("Password", authPassword);

		String htmlContent = TestDashUtil.getHTMLContentWithAuth(tttViewURL,
				authURL, authProps);

		return parseContent(htmlContent, phaseName, colIndexes);
	}

	private ERStatusRecord parseContent(String htmlContent, String phaseName,
			int[] colIndexes) {
		ERStatusRecord esr = null;
		// create an instance of HtmlCleaner
		HtmlCleaner cleaner = new HtmlCleaner();

		try {
			TagNode node = cleaner.clean(htmlContent);
			TagNode body = node.getElementsByName("body", false)[0];
			TagNode form = node.getElementsByName("form", true)[0];
			TagNode dataTable = form.getElementsByName("table", false)[3];
			TagNode[] dataTableTrs = dataTable.getElementsByName("tr", true);

			TagNode targetTr = null;
			for (int i = 0; i < dataTableTrs.length; i++) {
				TagNode tr = dataTableTrs[i];
				TagNode[] subTables = tr.getElementsByName("table", true);
				if (subTables != null && subTables.length == 1) {
					TagNode[] tds = subTables[0].getChildTags()[0]
							.getChildTags();
					TagNode phaseNameTd = tds[tds.length - 1];
					if (phaseNameTd.getText().toString().trim().equals(
							phaseName)) {
						targetTr = tr;
						break;
					}
				} else {
					continue;
				}
			}

			// TagNode targetTr = dataTableTrs[3];

			List tds = targetTr.getElementListByName("td", false);

			long unattempted = 0;
			long blocked = 0;
			long attempted = 0;
			long failed = 0;
			long permfailed = 0;
			long complete = 0;
			long deferred = 0;

			if (colIndexes[0] != -1)
				unattempted = Long.parseLong(((TagNode) tds.get(colIndexes[0]))
						.getText().toString());
			if (colIndexes[1] != -1)
				blocked = Long.parseLong(((TagNode) tds.get(colIndexes[1]))
						.getText().toString());
			if (colIndexes[2] != -1)
				attempted = Long.parseLong(((TagNode) tds.get(colIndexes[2]))
						.getText().toString());
			if (colIndexes[3] != -1)
				failed = Long.parseLong(((TagNode) tds.get(colIndexes[3]))
						.getText().toString());
			if (colIndexes[4] != -1)
				permfailed = Long.parseLong(((TagNode) tds.get(colIndexes[4]))
						.getText().toString());
			if (colIndexes[5] != -1)
				complete = Long.parseLong(((TagNode) tds.get(colIndexes[5]))
						.getText().toString());
			if (colIndexes[6] != -1)
				deferred = Long.parseLong(((TagNode) tds.get(colIndexes[6]))
						.getText().toString());

			esr = new ERStatusRecord(attempted, blocked, complete, deferred,
					failed, permfailed, unattempted);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return esr;
	}

}
