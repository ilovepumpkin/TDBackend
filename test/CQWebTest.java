import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import testdash.model.ERStatusRecord;

public class CQWebTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String jazzBugQueryCSVURL="https://ibmrpm.ibm.com:9443/jazz/resource/itemOid/com.ibm.team.workitem.query.QueryDescriptor/_XAycNfdEEd2AgJsk4CEKUQ?_mediaType=text/csv";
//		String jazzSecurityURL="https://ibmrpm.ibm.com:9443/jazz/j_security_check";
//		String jazzUsername="shenrui";
//		String jazzPassword="shenrui";
//		Properties authParams = new Properties();
//		authParams.put("j_username", jazzUsername);
//		authParams.put("j_password", jazzPassword);
//
//		String csvData = getHTMLContentWithAuth(
//				jazzBugQueryCSVURL, jazzSecurityURL, authParams);
//		System.out.println(csvData);
		
		// String targetURL =
		// "https://bst-cq01.notesdev.ibm.com/cqweb/main?command=GenerateMainFrame"
		// ;
		String targetURL = "https://bst-cq01.notesdev.ibm.com/cqweb/login?/cqweb/main?command=GenerateMainFrame";
		// String targetURL="https://bst-cq01.notesdev.ibm.com/cqweb/login";
		String authURL = "https://bst-cq01.notesdev.ibm.com/cqweb/login";
		Properties props = new Properties();
		props.put("targetUrl", "/cqweb/main?command=GenerateMainFrame");
		props.put("ratl_userdb", "BowSt");
		props.put("test", "");
		//props.put("clientServerAddress", "https://bst-cq01.notesdev.ibm.com/cqweb/login?/cqweb/main?command=GenerateMainFrame");
		props.put("username", "shenrui@cn.ibm.com");
		props.put("password", "jvj8t98d");
		props.put("schema", "BST-CQ01");
		props.put("userDb", "BowSt");
		String content = getHTMLContentWithAuth(targetURL, authURL, props);
		System.out.println(parseHTML(content));

//		// httpClient(targetURL);
	}
	
	public static String parseHTML(String htmlContent){
		htmlContent=htmlContent.replace("&nbsp;", "");
		
		HtmlCleaner cleaner = new HtmlCleaner();

		try {
			TagNode node = cleaner.clean(htmlContent);
			List tables = node.getElementListByName("table", true);
					
			TagNode dataTable = (TagNode) tables.get(7);
			List trs = dataTable.getElementListByName("tr", true);
			for (Iterator iter = trs.iterator(); iter.hasNext();) {
				TagNode tr = (TagNode) iter.next();
				
				List tds=tr.getElementListByName("td", true);
				for(Iterator iter1 = tds.iterator(); iter1.hasNext();){
					TagNode td = (TagNode) iter1.next();
					System.out.println("$$$$$"+td.getText());
				}
//				if (tr.getText().indexOf(phaseName) != -1) {
//					List tds = tr.getElementListByName("td", false);
//					long unattempted = Long.parseLong(((TagNode) tds.get(1))
//							.getText().toString());
//					long attempted = Long.parseLong(((TagNode) tds.get(2))
//							.getText().toString());
//					long blocked = Long.parseLong(((TagNode) tds.get(3))
//							.getText().toString());
//					long failed = Long.parseLong(((TagNode) tds.get(4))
//							.getText().toString());
//					long complete = Long.parseLong(((TagNode) tds.get(5))
//							.getText().toString());
//					long permfailed = Long.parseLong(((TagNode) tds.get(6))
//							.getText().toString());
//					long deferred = Long.parseLong(((TagNode) tds.get(7))
//							.getText().toString());
//
//					esr = new ERStatusRecord(attempted, blocked, complete,
//							deferred, failed, permfailed, unattempted);
//					break;
//				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static String httpClient(String url) {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				String response = method.getResponseBodyAsString();
				System.out.println(response);
				return response;
			}

		} catch (HttpException e) {
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getHTMLContentWithAuth(String targetURL, String authURL,
			Properties authParams) {
		String httpContent = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = null;
		PostMethod postMethod = null;

		try {
			int int_result = 0;
			String contents="";
//			getMethod = new GetMethod(targetURL);
//			getMethod.setFollowRedirects(true);
//			int_result = httpClient.executeMethod(getMethod);
//			String contents = getMethod.getResponseBodyAsString();
//			getMethod.releaseConnection();

			// pass the authentication
			postMethod = new PostMethod(authURL);

			Enumeration emParams = authParams.propertyNames();

			while (emParams.hasMoreElements()) {
				String paramName = (String) emParams.nextElement();
				String paramValue = authParams.getProperty(paramName);
				postMethod.addParameter(paramName, paramValue);
			}

			int_result = httpClient.executeMethod(postMethod);
			System.out.println(">>>Post method status code:"+int_result);
			
			Header locationHeader=postMethod.getResponseHeader("Location");
			String location=locationHeader.getValue();
			
			contents = postMethod.getResponseBodyAsString();
			postMethod.releaseConnection();

			// postMethod = new PostMethod(csvQueryURL);
			// int_result = httpClient.executeMethod(postMethod);
			// httpContent = postMethod.getResponseBodyAsString();

//			GetMethod get = new GetMethod(targetURL);
//			
//			int_result = httpClient.executeMethod(get);
//			
//			Header[] headers=get.getRequestHeaders();
//			for(int i=0;i<headers.length;i++){
//				Header header=headers[i];
//				System.out.println("#####"+header);
//			}
//			
//			Header[] resHeaders=get.getResponseHeaders();
//			for(int i=0;i<resHeaders.length;i++){
//				Header header=resHeaders[i];
//				System.out.println("%%%%"+header);
//			}
//			
//			httpContent = get.getResponseBodyAsString();
			
			
			location=location.replace("GenerateMainFrame", "GetRecords");
			location=location+"&dbid=33750254&qid=33750254";
//			GetMethod get2 = new GetMethod(location);
//			get2.setFollowRedirects(true);
//			get2.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
//			int_result = httpClient.executeMethod(get2);
//			
//			Header[] headers=get2.getRequestHeaders();
//			for(int i=0;i<headers.length;i++){
//				Header header=headers[i];
//				System.out.println("#####"+header);
//			}
//			
//				
//			Header[] respheaders=get2.getResponseHeaders();
//			for(int i=0;i<respheaders.length;i++){
//				Header header=respheaders[i];
//				System.out.println("^^^^^"+header);
//			}
//			
//			httpContent = get2.getResponseBodyAsString();
//			InputStream is=get2.getResponseBodyAsStream();
//			System.out.println(">>>>>"+convertStreamToString(is));
			
			
			// pass the authentication
			String dataURL=location;
			System.out.println(dataURL);
			PostMethod postMethod2 = new PostMethod(dataURL);
			
			Properties dataParams=new Properties();
			dataParams.put("recordsPerPage", "All");
			dataParams.put("currentPageSetStart", "0");

			Enumeration dParams = dataParams.propertyNames();

			while (dParams.hasMoreElements()) {
				String paramName = (String) dParams.nextElement();
				String paramValue = dataParams.getProperty(paramName);
				postMethod2.addParameter(paramName, paramValue);
			}

			postMethod2.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
			
			int_result = httpClient.executeMethod(postMethod2);
			System.out.println(">>>Post method status code:"+int_result);
			
			httpContent=postMethod2.getResponseBodyAsString();
			//System.out.println(">>>>>>>>>"+httpContent);
			postMethod2.releaseConnection();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpContent;
	}
	
	private static String convertStreamToString(InputStream is) {
		/*
		 * To conver the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

}
