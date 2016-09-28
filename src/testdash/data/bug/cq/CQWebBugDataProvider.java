package testdash.data.bug.cq;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import testdash.data.bug.BugDataProvider;
import testdash.exception.TestDashException;
import testdash.model.BugRecord;
import testdash.model.TeamContext;

public class CQWebBugDataProvider implements BugDataProvider {
	private String datePattern;
	private String rmsessionid;

	public List getBugList(TeamContext tc,String id) {
		//String cqwebSecurityURL = tc.getProperty("cqwebSecurityURL");
		String queryURL = tc.getProperty("queryURL");
		String dbName = tc.getProperty("dbName");
		String userName = tc.getProperty("userName");
		String password = tc.getProperty("password");
		String schema = tc.getProperty("schema");
		String defectLinkTemplate = tc.getProperty("defectLinkTemplate");
		datePattern = tc.getProperty("datePattern");
		if(datePattern==null)
			datePattern = "yyyy-MM-dd hh:mm:ss";

		Properties authParams = new Properties();
		authParams.put("targetUrl", "/cqweb/main?command=GenerateMainFrame");
		authParams.put("ratl_userdb", dbName);
		authParams.put("test", "");
		//props.put("clientServerAddress", "https://bst-cq01.notesdev.ibm.com/cqweb/login?/cqweb/main?command=GenerateMainFrame");
		authParams.put("username", userName);
		authParams.put("password", password);
		authParams.put("schema", schema);
		authParams.put("userDb", dbName);
		
		int splitIndex=queryURL.indexOf("/main");
		String cqwebSecurityURL=queryURL.substring(0,splitIndex)+"/login";
		System.out.println("****"+cqwebSecurityURL);
		
		String htmlContent = getHTMLContent(cqwebSecurityURL,authParams,queryURL);
		
		return constructBugList(htmlContent,defectLinkTemplate);
	}
	
	private String getHTMLContent(String authURL,
			Properties authParams,String queryURL) {
		String httpContent = "";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = null;

		try {
			int int_result = 0;
			String contents="";

			// pass the authentication
			postMethod = new PostMethod(authURL);

			Enumeration emParams = authParams.propertyNames();

			while (emParams.hasMoreElements()) {
				String paramName = (String) emParams.nextElement();
				String paramValue = authParams.getProperty(paramName);
				postMethod.addParameter(paramName, paramValue);
			}

			int_result = httpClient.executeMethod(postMethod);
						
			Header locationHeader=postMethod.getResponseHeader("Location");
			
			String location=locationHeader.getValue();
			
			rmsessionid=location.substring(location.lastIndexOf("=")+1);
			
			postMethod.releaseConnection();
			
			/*
			 * Retrieve the html content of displaying result data table
			 */
			queryURL=queryURL.replace("${rmsessionid}", rmsessionid);
			PostMethod postMethod2 = new PostMethod(queryURL);
			postMethod2.addParameter("recordsPerPage", "All");
			postMethod2.addParameter("currentPageSetStart", "0");
			postMethod2.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
			
			int_result = httpClient.executeMethod(postMethod2);
			
			httpContent=postMethod2.getResponseBodyAsString();
			postMethod2.releaseConnection();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpContent;
	}

	private List constructBugList(String htmlContent,String cqBugLinkTemplate){
		
		List bugList = new ArrayList();
		htmlContent=htmlContent.replace("&nbsp;", "");
		
		HtmlCleaner cleaner = new HtmlCleaner();

		try {
			TagNode node = cleaner.clean(htmlContent);
			List tables = node.getElementListByName("table", true);
					
			TagNode dataTable = (TagNode) tables.get(7);
			List trs = dataTable.getElementListByName("tr", true);
			trs.remove(0); //remote the header row
			for (Iterator iter = trs.iterator(); iter.hasNext();) {
				TagNode tr = (TagNode) iter.next();
					
				TagNode[] tds=tr.getElementsByName("td", true);
								
				BugRecord br = new BugRecord();

				String id = tds[1].getText().toString();
				br.setId(id);
				br.setStatus(tds[5].getText().toString());
				br.setSeverity(tds[3].getText().toString());
				br.setPriority(tds[4].getText().toString());
				br.setOwner(tds[8].getText().toString());
				br.setReporter(tds[7].getText().toString());

//				TagNode idHref=tds[0].getElementsByName("a", true)[0];
//				String href=idHref.getAttributeByName("href");
//				String[] pieces=href.split("&");
//				String entityId=pieces[2].split("=")[1];
//				String cqDefectLink=cqBugLinkTemplate.replace("${rmsessionid}", rmsessionid);
//				cqDefectLink=cqDefectLink.replace("${entityId}",entityId);
//				br.setWebLink(cqDefectLink);

				String creationDateStr = tds[6].getText().toString();
				SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
				
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

				String summary = tds[2].getText().toString();
				summary = summary.substring(0, summary.length() - 1);
				br.setSummary(summary);
				bugList.add(br);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bugList;
	}
}
