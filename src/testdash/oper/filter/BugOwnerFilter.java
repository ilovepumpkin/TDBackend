package testdash.oper.filter;

import testdash.model.BugRecord;



public class BugOwnerFilter extends BaseBugFilter {

	public BugOwnerFilter(String valueStr) {
		super(valueStr);
	}

	@Override
	public String getBugFieldValue(BugRecord br) {
		return br.getOwner();
	}
}
