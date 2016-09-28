package testdash.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;

import testdash.rest.resource.ERStatusResource;
import testdash.rest.resource.BugGroupingResource;
import testdash.rest.resource.BugListResource;
import testdash.rest.resource.SCurveResource;
import testdash.rest.resource.TeamListResource;

public class TestDashApplication extends Application {

	@Override
	public Restlet createRoot() {
		Router router = new Router(getContext());
		router.attach("/teamlist", TeamListResource.class);
		/*
		 * type=er,defect
		 */
		router.attach("/scurve/{type}/{team}", SCurveResource.class);
		router.attach("/erstatus/{format}/{team}", ERStatusResource.class);
		
		router.attach("/buglist/{team}", BugListResource.class);
		/*
		 * type=groupby
		 */
		router.attach("/buggroupby/{team}",
				BugGroupingResource.class);

		return router;
	}
}
