package testdash.oper.groupby;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import testdash.model.BugRecord;
import testdash.oper.BugGrouper;

public abstract class BaseBugGrouper implements BugGrouper {

	public Map group(List bugList) {
		Map counterMap = new TreeMap();

		for (int i = 0; i < bugList.size(); i++) {
			BugRecord br = (BugRecord) bugList.get(i);
			String groupByFieldValue = getGroupByFieldValue(br);

			Object counter = counterMap.get(groupByFieldValue);
			if (counter == null) {
				counter = new Integer(0);
			}
			int count = ((Integer) counter).intValue();
			counterMap.put(groupByFieldValue, Integer.valueOf(count + 1));
		}
		return counterMap;
	}

	protected abstract String getGroupByFieldValue(BugRecord br);

}
