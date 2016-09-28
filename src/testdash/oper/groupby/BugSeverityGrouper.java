package testdash.oper.groupby;

import testdash.model.BugRecord;

public class BugSeverityGrouper extends BaseBugGrouper {
	@Override
	protected String getGroupByFieldValue(BugRecord br) {
		return br.getSeverity();
	}
}
