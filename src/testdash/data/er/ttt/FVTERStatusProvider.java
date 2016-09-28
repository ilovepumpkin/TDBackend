package testdash.data.er.ttt;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import testdash.data.er.ERStatusProvider;
import testdash.exception.TestDashException;
import testdash.model.ERStatusRecord;
import testdash.model.TeamContext;
import testdash.util.TestDashUtil;

public class FVTERStatusProvider implements ERStatusProvider {
	private final static String TTT_VIEW_URL = "tttViewURL";
	private final static String PHASE_NAME = "phaseName";
	private final static String STATUS_ORDER = "unattempted,blocked,attempted,failed,permfailed,complete,deferred";

	public ERStatusRecord getERStatusRecord(TeamContext tc, String id) {
		String tttViewURLAttr = TTT_VIEW_URL;
		String phaseNameAttr = PHASE_NAME;
		String statusOrderAttr = STATUS_ORDER;

		if (id != null) {
			tttViewURLAttr = id + "." + tttViewURLAttr;
			phaseNameAttr = id + "." + phaseNameAttr;
			statusOrderAttr = id + "." + statusOrderAttr;
		}

		String columns = tc
				.getProperty(statusOrderAttr);

		// apply default order if it is not defined
		if(columns==null)
			columns="1,2,3,4,5,6,7";
		
		int[] colIndexes = new int[7];
		StringTokenizer st = new StringTokenizer(columns, ",");
		int i = 0;
		while (st.hasMoreTokens()) {
			int colIndex = Integer.parseInt(st.nextToken());
			colIndexes[i++] = colIndex;
		}

		String tttViewURL = tc.getProperty(tttViewURLAttr);
		String phaseName = tc.getProperty(phaseNameAttr);
		
		if(tttViewURL==null) 
			throw new TestDashException("The properity "+tttViewURLAttr+" should be predefined.");
		
		if(phaseName==null)
			throw new TestDashException("The properity "+phaseNameAttr+" should be predefined.");
		
		String htmlContent = TestDashUtil.getHTMLContentWithoutAuth(tttViewURL);
		return parseContent(htmlContent, phaseName,colIndexes);
	}

	private ERStatusRecord parseContent(String htmlContent, String phaseName,int[] colIndexes) {
		ERStatusRecord esr = null;
		// create an instance of HtmlCleaner
		HtmlCleaner cleaner = new HtmlCleaner();

		try {
			TagNode node = cleaner.clean(htmlContent);
			List tables = node.getElementListByName("table", true);
			TagNode dataTable = (TagNode) tables.get(1);
			List trs = dataTable.getElementListByName("tr", true);
			for (Iterator iter = trs.iterator(); iter.hasNext();) {
				TagNode tr = (TagNode) iter.next();
				if (tr.getText().indexOf(phaseName) != -1) {
					List tds = tr.getElementListByName("td", false);
					
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
					
					esr = new ERStatusRecord(attempted, blocked, complete,
							deferred, failed, permfailed, unattempted);
					break;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return esr;
	}

}
