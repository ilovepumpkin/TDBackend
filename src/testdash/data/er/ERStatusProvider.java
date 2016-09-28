package testdash.data.er;

import testdash.model.ERStatusRecord;
import testdash.model.TeamContext;

public interface ERStatusProvider {
	public ERStatusRecord getERStatusRecord(TeamContext tc,String id);
}
