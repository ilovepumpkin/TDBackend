package testdash.util;

public class XmlUtil {
	public static String normalize(String xmlStr) {
		xmlStr = xmlStr.replaceAll("&", "&amp;");
		xmlStr = xmlStr.replaceAll("<", "&lt;");
		xmlStr = xmlStr.replaceAll(">", "&gt;");
		xmlStr = xmlStr.replaceAll("\"", "&quot;");
		xmlStr = xmlStr.replaceAll(" ", "&nbsp;");
		xmlStr = xmlStr.replaceAll("©", "&copy;");
		xmlStr = xmlStr.replaceAll("®", "&reg");
		return xmlStr;
	}
}
