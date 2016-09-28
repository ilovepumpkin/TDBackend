package testdash.oper.filter;

import testdash.model.BugRecord;

public class BugStatusFilter extends BaseBugFilter {

	public BugStatusFilter(String valueStr) {
		super(valueStr);
	}

	@Override
	public String getBugFieldValue(BugRecord br) {
		return br.getStatus();
	}

}
