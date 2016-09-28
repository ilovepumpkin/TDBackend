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
import testdash.exception.TestDashException;
import testdash.oper.BugGrouper;
import testdash.oper.groupby.BugOwnerGrouper;
import testdash.oper.groupby.BugReporterGrouper;
import testdash.oper.groupby.BugSeverityGrouper;
import testdash.oper.groupby.BugStatusGrouper;

public class BugGroupingResource extends BugResource {
	private String type;
	private String groupBy;

	public BugGroupingResource(Context context, Request request,
			Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));

		groupBy = request.getResourceRef().getQueryAsForm().getFirstValue(
				"groupBy");
	}

	public Representation getRepresentation(Variant variant) {

		BugGrouper bugGrouper = null;
		if (this.groupBy == null) {
			throw new TestDashException("The parameter groupBy is required.");
		} else {
			if (this.groupBy.equalsIgnoreCase("severity")) {
				bugGrouper = new BugSeverityGrouper();
			} else if (this.groupBy.equalsIgnoreCase("owner")) {
				bugGrouper = new BugOwnerGrouper();
			} else if (this.groupBy.equalsIgnoreCase("reporter")) {
				bugGrouper = new BugReporterGrouper();
			} else if (this.groupBy.equalsIgnoreCase("status")) {
				bugGrouper = new BugStatusGrouper();
			} else {
				throw new TestDashException("The group method " + this.groupBy
						+ " is not supported.");
			}
		}

		BugDataManager bugDataManager = BugDataManager.getInstance();
		String retString = bugDataManager.groupBy("xml", bugGrouper, tc,id);

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
