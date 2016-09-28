package testdash.data.bug;

import java.util.List;

import testdash.model.TeamContext;

public interface BugDataProvider {
	public List getBugList(TeamContext tc,String id);
}
