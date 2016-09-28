package testdash.oper.groupby;

import testdash.model.BugRecord;

public class BugReporterGrouper extends BaseBugGrouper {

	@Override
	protected String getGroupByFieldValue(BugRecord br) {
		// TODO Auto-generated method stub
		return br.getReporter();
	}
}
