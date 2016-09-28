package testdash.rest.resource;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import testdash.data.BugDataManager;

public class BugResource extends BaseResource {
	protected String severity;
	protected String bugOwner;
	protected String bugReporter;
	protected String bugStatus;	
	protected String id;	
	
	protected String jazzSecurityURL;
	protected String jazzBugQueryCSVURL;
	protected String jazzUsername;
	protected String jazzPassword;

	public BugResource(Context context, Request request, Response response) {
		super(context, request, response);
		// if there is no severity parameter, the default value is 'All'
		severity = request.getResourceRef().getQueryAsForm().getFirstValue(
				"severity");

		bugOwner = request.getResourceRef().getQueryAsForm().getFirstValue(
				"owner");
		
		bugReporter = request.getResourceRef().getQueryAsForm().getFirstValue(
		"reporter");
		
		bugStatus = request.getResourceRef().getQueryAsForm().getFirstValue(
		"status");
		
		id = request.getResourceRef().getQueryAsForm().getFirstValue(
		"id");
	}

}
