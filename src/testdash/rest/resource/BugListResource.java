package testdash.rest.resource;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import testdash.data.BugDataManager;
import testdash.oper.FilterList;
import testdash.oper.filter.BaseBugFilter;
import testdash.oper.filter.BugOwnerFilter;
import testdash.oper.filter.BugReporterFilter;
import testdash.oper.filter.BugSeverityFilter;
import testdash.oper.filter.BugStatusFilter;

public class BugListResource extends BugResource {

	public BugListResource(Context context, Request request,
			Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public Representation getRepresentation(Variant variant) {
		String retString = null;

		FilterList fl = new FilterList();
		fl.add(new BugSeverityFilter(severity));
		fl.add(new BugOwnerFilter(bugOwner));
		fl.add(new BugReporterFilter(bugReporter));
		fl.add(new BugStatusFilter(bugStatus));

		BugDataManager bugDataManager = BugDataManager.getInstance();

		retString = bugDataManager.getOpenBugs("feed", fl, tc,id);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new InputSource(new StringReader(
					retString)));

			DomRepresentation xmlRep = new DomRepresentation(
					MediaType.TEXT_XML, doc);

			return xmlRep;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
