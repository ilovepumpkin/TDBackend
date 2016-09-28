package testdash.rest.resource;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import testdash.config.TeamContextManager;
import testdash.model.TeamContext;

public class BaseResource extends Resource {
	protected String team;
	protected TeamContext tc;
	
	public BaseResource(Context context, Request request, Response response) {
		super(context, request, response);

		team = (String) getRequest().getAttributes().get("team");
		tc=TeamContextManager.getInstance().getTeamContext(team);
	}
	
	protected TeamContext getTeamContext(){
		return tc;
	}
}
