package testdash.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;

import testdash.exception.TestDashException;

public class TestDashUtil {
	public static HttpClient formAuth(String authURL,Properties authParams){
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// pass the authentication
			postMethod = new PostMethod(authURL);

			Enumeration emParams = authParams.propertyNames();

			while (emParams.hasMoreElements()) {
				String paramName = (String) emParams.nextElement();
				String paramValue = authParams.getProperty(paramName);
				postMethod.addParameter(paramName, paramValue);
			}

			httpClient.executeMethod(postMethod);
			postMethod.releaseConnection();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpClient;
	}
	
	public static String getHTMLContentWithoutAuth(String targetURL) {
		Client client = new Client(Protocol.HTTP);
		
		Reference dataUri = new Reference(targetURL);

		Response response = client.get(dataUri);
		if (response.getStatus().isSuccess()) {
			if (response.isEntityAvailable()) {
				try {
					return response.getEntity().getText();
				} catch (IOException e) {
					throw new TestDashException(
							"Cannot get content via the URL - " + targetURL, e);
				}
			}
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
			getMethod = new GetMethod(targetURL);
			getMethod.setFollowRedirects(true);
			int_result = httpClient.executeMethod(getMethod);
			String contents = getMethod.getResponseBodyAsString();
			getMethod.releaseConnection();

			// pass the authentication
			postMethod = new PostMethod(authURL);

			Enumeration emParams = authParams.propertyNames();

			while (emParams.hasMoreElements()) {
				String paramName = (String) emParams.nextElement();
				String paramValue = authParams.getProperty(paramName);
				postMethod.addParameter(paramName, paramValue);
			}

			int_result = httpClient.executeMethod(postMethod);
			contents = postMethod.getResponseBodyAsString();
			postMethod.releaseConnection();

			// postMethod = new PostMethod(csvQueryURL);
			// int_result = httpClient.executeMethod(postMethod);
			// httpContent = postMethod.getResponseBodyAsString();

			GetMethod get = new GetMethod(targetURL);
			int_result = httpClient.executeMethod(get);
			httpContent = get.getResponseBodyAsString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpContent;
	}

}
