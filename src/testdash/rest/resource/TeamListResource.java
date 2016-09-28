package testdash.rest.resource;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import testdash.config.TeamContextManager;
import testdash.model.TeamContext;

public class TeamListResource extends Resource {
	private String rootRef;

	public TeamListResource(Context context, Request request, Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));
		rootRef=request.getRootRef().toString();		
	}
	
	private void constructServiceURLPart(Document d,Element parentElement,String teamShortName){
		Element erStatusXMLElem=d.createElement("ERStatusXMLURL");
		erStatusXMLElem.appendChild(d.createTextNode(rootRef+"/erstatus/xml/"+teamShortName));
		parentElement.appendChild(erStatusXMLElem);
		
		Element erStatusFeedElem=d.createElement("ERStatusFeedURL");
		erStatusFeedElem.appendChild(d.createTextNode(rootRef+"/erstatus/feed/"+teamShortName));
		parentElement.appendChild(erStatusFeedElem);
		
		Element erStatusCSVElem=d.createElement("ERStatusCSVURL");
		erStatusCSVElem.appendChild(d.createTextNode(rootRef+"/erstatus/csv/"+teamShortName));
		parentElement.appendChild(erStatusCSVElem);
		
		Element erSCurveElem=d.createElement("ERSCurveURL");
		erSCurveElem.appendChild(d.createTextNode(rootRef+"/scurve/er/"+teamShortName));
		parentElement.appendChild(erSCurveElem);
		
		Element defectSCurveElem=d.createElement("DefectSCurveURL");
		defectSCurveElem.appendChild(d.createTextNode(rootRef+"/scurve/defect/"+teamShortName));
		parentElement.appendChild(defectSCurveElem);
		
		Element bugListElem=d.createElement("BugListURL");
		bugListElem.appendChild(d.createTextNode(rootRef+"/buglist/"+teamShortName));
		parentElement.appendChild(bugListElem);
		
		Element bugGroupBylem=d.createElement("BugGroupByURL");
		bugGroupBylem.appendChild(d.createTextNode(rootRef+"/buggroupby/"+teamShortName));
		parentElement.appendChild(bugGroupBylem);
	}

	public Representation getRepresentation(Variant variant) {
		Map tcMap = TeamContextManager.getInstance().getTeamContextMap();
		Iterator iTcMap = tcMap.keySet().iterator();
		
		try {
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			Document d = representation.getDocument();
			Element rowSet = d.createElement("TeamList");
			d.appendChild(rowSet);
			while (iTcMap.hasNext()) {
				Element row = d.createElement("Team");
				String teamShortName = (String) iTcMap.next();
				TeamContext tc = (TeamContext) tcMap.get(teamShortName);
				Element teamLabel = d.createElement("FullName");
				teamLabel.appendChild(d.createTextNode(tc.getFullName()));
				row.appendChild(teamLabel);
				Element teamValue = d.createElement("ShortName");
				teamValue.appendChild(d.createTextNode(teamShortName));
				row.appendChild(teamValue);
				
				constructServiceURLPart(d,row,teamShortName);
				
				rowSet.appendChild(row);
			}
			d.normalizeDocument();
			return representation;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
