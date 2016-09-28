package testdash.data;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import testdash.exception.TestDashException;
import testdash.model.BugRecord;
import testdash.model.TeamContext;
import testdash.oper.BugFilter;
import testdash.oper.BugGrouper;
import testdash.oper.FilterList;
import testdash.util.XmlUtil;

public class BugDataManager {

	private static BugDataManager instance;

	private BugDataManager() {

	}

	public static BugDataManager getInstance() {
		if (instance == null) {
			instance = new BugDataManager();
		}
		return instance;
	}

	public String getOpenBugs(String format, FilterList fl, TeamContext tc,String id) {
		List openBugList = getOpenBugList(tc,id);
		List filteredBugList = new ArrayList();
		/*
		 * Filter bug list according to the filter list provided.£Æ
		 */
		Iterator iOpenBugs = openBugList.iterator();
		while (iOpenBugs.hasNext()) {
			BugRecord br = (BugRecord) iOpenBugs.next();
			if (accept(br, fl)) {
				filteredBugList.add(br);
			}
		}
		return getBugListAsFeed(filteredBugList);
	}

	private String getBugListAsFeed(List bugList) {
		Feed feed = new Abdera().newFeed();
		feed.setId("tag:testdash");
		feed.setTitle("TestDash Bug List");
		feed.setSubtitle("TestDash Bug List");
		feed.setUpdated(new Date());
		feed.addAuthor("Rui Shen");
		feed.addLink("http://w3.tap.ibm.com/sae");
		
		for (int i = 0; i < bugList.size(); i++) {
			BugRecord br = (BugRecord) bugList.get(i);
			
			Entry entry = feed.addEntry();
			entry.setId(br.getId());
			entry.setTitle(br.getSeverity()+"-"+br.getOwner()+"-"+br.getSummary());
			entry.setSummaryAsHtml(br.toHTML());
			entry.setUpdated(br.getCreationDate());
			entry.setPublished(br.getCreationDate());
			entry.addLink(XmlUtil.normalize(br.getWebLink()),"blank");
			
			String brXml=br.toXml();
			brXml=brXml.replaceAll("Bug>", "row>");
			entry.setContentAsXhtml(brXml);			
		}
		return feed.toString();
	}

	private boolean accept(BugRecord br, FilterList fl) {
		boolean accept = true;
		for (int i = 0; i < fl.size(); i++) {
			BugFilter filter = fl.getFilter(i);

			if (filter.accept(br)) {
				continue;
			} else {
				accept = false;
				return accept;
			}
		}
		return accept;
	}

	private List getOpenBugList(TeamContext tc,String id) {
		try {
			Class clazz = Class.forName(tc.getBugDataProviderClassName());
			Method m = clazz.getMethod("getBugList", TeamContext.class,String.class);
			List openBugList = (List) m.invoke(clazz.newInstance(),
					new Object[] { tc,id });
			return openBugList;
		} catch (Exception e) {
			throw new TestDashException("Fail to get open bug list", e);
		}
	}

	public String groupBy(String format, BugGrouper grouper, TeamContext tc,String id) {
		List openBugList = getOpenBugList(tc,id);
		return genBugGroupByXml(grouper.group(openBugList));
	}

	private String genBugGroupByXml(Map groupByMap) {
		StringBuffer xmlData = new StringBuffer("<RowSet>");
		Iterator iGroupBy = groupByMap.keySet().iterator();
		while (iGroupBy.hasNext()) {
			String fieldValue = (String) iGroupBy.next();
			String count = groupByMap.get(fieldValue).toString();

			xmlData.append("<Row><GroupBy>").append(fieldValue).append(
					"</GroupBy>").append("<CalValue>").append(count).append(
					"</CalValue>").append("</Row>");

		}
		xmlData.append("</RowSet>");
		return xmlData.toString();
	}
}
