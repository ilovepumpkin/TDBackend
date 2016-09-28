package testdash.rest.resource;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import testdash.data.ERStatusManager;

public class ERStatusResource extends BaseResource {
	private String format = null;
	private String id=null;

	public ERStatusResource(Context context, Request request, Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));

		this.format = (String) getRequest().getAttributes().get("format");
		this.id = request.getResourceRef().getQueryAsForm().getFirstValue(
		"id");
	}

	public Representation getRepresentation(Variant variant) {
		ERStatusManager erStatusManager = ERStatusManager.getInstance();
		String statusData = erStatusManager.getERStatus(format, tc,id);
		StringRepresentation sr = new StringRepresentation(statusData);
		return sr;
	}
}
