package testdash.oper.groupby;

import testdash.model.BugRecord;

public class BugOwnerGrouper extends BaseBugGrouper {

	@Override
	protected String getGroupByFieldValue(BugRecord br) {
		// TODO Auto-generated method stub
		return br.getOwner();
	}
}
