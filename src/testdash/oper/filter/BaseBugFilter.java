package testdash.oper.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import testdash.model.BugRecord;
import testdash.oper.BugFilter;

public abstract class BaseBugFilter implements BugFilter {
	protected List valueList;

	public BaseBugFilter(List acceptedValueList) {
		super();
		this.valueList = acceptedValueList;
	}

	public BaseBugFilter(String acceptedValueStr) {
		super();
		this.valueList = convertStringToList(acceptedValueStr);
	}

	public List getValueList() {
		return valueList;
	}

	private List convertStringToList(String commaStr) {
		List sevList = new ArrayList();
		if (commaStr != null) {
			StringTokenizer st = new StringTokenizer(commaStr, ",");
			while (st.hasMoreTokens()) {
				sevList.add(st.nextToken());
			}
		}
		return sevList;
	}

	public boolean accept(BugRecord br) {
		if (valueList.isEmpty())
			return true;

		for (int i = 0; i < valueList.size(); i++) {
			String value = (String) valueList.get(i);
			if (getBugFieldValue(br).indexOf(value) != -1) {
				return true;
			}
		}
		return false;
	}

	public abstract String getBugFieldValue(BugRecord br);

}
