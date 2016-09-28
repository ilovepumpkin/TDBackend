package testdash.oper;

import testdash.model.BugRecord;

public interface BugFilter {
	public boolean accept(BugRecord br);
}
