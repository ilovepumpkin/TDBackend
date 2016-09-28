package testdash.data.bug.tct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import testdash.data.bug.BugDataProvider;
import testdash.exception.TestDashException;
import testdash.model.BugRecord;
import testdash.model.TeamContext;

import com.ibm.tct.api.beans.ArticleInfo;
import com.ibm.tct.api.beans.ArticleListInfo;
import com.ibm.tct.api.client.business.TCTArticle;
import com.ibm.tct.api.client.business.TCTProject;
import com.ibm.tct.api.client.business.TCTSystem;

public class TCTBugDataProvider implements BugDataProvider {

	public List getBugList(TeamContext tc,String id) {
		String tctUsername = tc.getProperty("tctUsername");
		String tctPassword = tc.getProperty("tctPassword");
		String tctConfigFile = tc.getProperty("tctConfigFile");
		String tctKeywords = tc.getProperty("tctKeywords");
		String tctSearchColumn = tc.getProperty("tctSearchColumn");
		String tctSortBy = tc.getProperty("tctSortBy");
		String tctBugLinkTemplate = tc.getProperty("tctBugLinkTemplate");

		return getBugList(tctUsername, tctPassword, tctConfigFile, tctKeywords,
				tctSearchColumn, tctSortBy,tctBugLinkTemplate);
	}

	private List getBugList(String tctUsername, String tctPassword,
			String tctConfigFile, String tctKeywords, String tctSearchColumn,
			String tctSortBy,String tctBugLinkTemplate) {

		List bugList = new ArrayList();

		TCTSystem tctsystem = null;
		try {
			tctsystem = TCTSystem
					.logOn(tctUsername, tctPassword, tctConfigFile);
			TCTProject tctproject = tctsystem.getTCTProject("LMM11");
			Vector articles = tctproject.searchArticle(tctKeywords,
					tctSearchColumn, null, null, tctSortBy);

			for (int i = 0; i < articles.size(); i++) {
				ArticleListInfo articleListInfo = (ArticleListInfo) articles
						.get(i);
				TCTArticle article = tctproject.getTCTArticle(articleListInfo
						.getKEY());
				ArticleInfo articleInfo = article.getArticleInfo();
				String id = articleInfo.getKEY().toString();
				String reporter = articleInfo.getORIGINATOR().getUSERNAME();
				String status = articleInfo.getSTATUS();
				String summary = articleInfo.getSUBJECT();
				Date creationDate = articleInfo.getCREATEDATE();
				String priority = articleInfo.getPRIORITY().toString();

				BugRecord br = new BugRecord();
				br.setId(id);
				br.setOwner("");
				br.setPriority(priority);
				br.setReporter(reporter);
				br.setSeverity("");
				br.setStatus(status);
				br.setSummary(summary);
				br.setCreationDate(creationDate);
				br.setWebLink(tctBugLinkTemplate.replace("${id}", id));
				bugList.add(br);
			}
		} catch (Throwable e) {
			throw new TestDashException(
					"Fail to get article infomations from TCT", e);
		} finally {
			try {
				TCTSystem.logOffDirectly(tctUsername, tctPassword,
						tctConfigFile);
			} catch (Throwable e) {
				throw new TestDashException("Fail to log off TCT", e);
			}
		}
		return bugList;
	}
}
