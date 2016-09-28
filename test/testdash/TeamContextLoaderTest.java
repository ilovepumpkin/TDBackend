package testdash;

import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import testdash.config.TeamContextLoader;

public class TeamContextLoaderTest extends TestCase{
	private TeamContextLoader loader;
	
	public void setUp(){
		loader=new TeamContextLoader();
	}
	
	public void testGetTeamContextMap(){
		Map teamContextMap=loader.getTeamContextMap();
		Assert.assertTrue(teamContextMap.size()>0);
		Assert.assertTrue(teamContextMap.get("mmfvt")!=null);
	}
}
