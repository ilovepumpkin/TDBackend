package testdash.config;

import java.util.Map;

import testdash.model.TeamContext;

public class TeamContextManager {
	private static TeamContextManager instance;
	private Map tcMap;
	
	private TeamContextManager(){
		TeamContextLoader tcLoader=new TeamContextLoader();
		tcMap=tcLoader.getTeamContextMap();
	}
	
	public static TeamContextManager getInstance(){
		if(instance==null){
			instance=new TeamContextManager();
		}
		return instance;
	}
	
	public TeamContext getTeamContext(String shortName){
		return (TeamContext) tcMap.get(shortName);
	}
	
	public Map getTeamContextMap(){
		return tcMap;
	}
}
