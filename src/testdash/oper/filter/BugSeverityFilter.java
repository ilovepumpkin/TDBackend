package testdash.oper.filter;

import testdash.model.BugRecord;

public class BugSeverityFilter extends BaseBugFilter {

	public BugSeverityFilter(String valueStr) {
		super(valueStr);
	}

	@Override
	public String getBugFieldValue(BugRecord br) {
		return br.getSeverity();
	}

}
