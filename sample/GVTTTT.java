import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


public class GVTTTT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GVTTTT ttt=new GVTTTT();
		String url="http://9.191.64.85/WPLC/LabProject/LMM11/LMM11_GVT_Testcases.nsf/0/f59aafaea58c551f4825752800238738";
		String result=ttt.getQueryCsv(url);
		System.out.println(result);
	}

	public String getQueryCsv(String csvQueryURL) {
		String csv = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = null;
		PostMethod postMethod = null;
		
		
		
		try {
			int int_result = 0;
			getMethod = new GetMethod(csvQueryURL);
			//getMethod.setFollowRedirects(true);
			int_result = httpClient.executeMethod(getMethod);
			String contents = getMethod.getResponseBodyAsString();
			getMethod.releaseConnection();

			String jazzSecurityUrl="http://9.191.64.85/names.nsf?Login";
			//String jazzSecurityUrl="http://9.191.64.85/names.nsf?Login";
			String jazzUserName="shenrui@cn.ibm.com";
			String jazzPassword="jvj8t98d";
			
			
			postMethod = new PostMethod(jazzSecurityUrl);
			
			postMethod.addParameter("Username", jazzUserName);
			
			postMethod.addParameter("Password", jazzPassword);
//			postMethod.addParameter("%%ModDate", "0000000800000100");
//			postMethod.addParameter("$PublicAccess", "1");
//			postMethod.addParameter("RedirectTo", "/WPLC/LabProject/LMM11/LMM11_GVT_Testcases.nsf");
//			postMethod.addParameter("ReasonType", "0");
			int_result = httpClient.executeMethod(postMethod);
			contents = postMethod.getResponseBodyAsString();
			postMethod.releaseConnection();

			postMethod = new PostMethod(csvQueryURL);
			int_result = httpClient.executeMethod(postMethod);
			csv = postMethod.getResponseBodyAsString();
			
//			GetMethod get=new GetMethod(csvQueryURL);
//			int_result = httpClient.executeMethod(get);
//			csv = get.getResponseBodyAsString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csv;
	}
}
