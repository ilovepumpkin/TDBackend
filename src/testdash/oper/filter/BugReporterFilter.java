package testdash.oper.filter;

import testdash.model.BugRecord;



public class BugReporterFilter extends BaseBugFilter {

	public BugReporterFilter(String valueStr) {
		super(valueStr);
	}

	@Override
	public String getBugFieldValue(BugRecord br) {
		return br.getReporter();
	}
}
