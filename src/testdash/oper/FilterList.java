package testdash.oper;

import java.util.ArrayList;

import testdash.oper.filter.BaseBugFilter;


public class FilterList extends ArrayList {
	public BugFilter getFilter(int index) {
		return (BaseBugFilter) this.get(index);
	}
	
	public void setFilter(int index,BugFilter filter){
		this.set(index, filter);
	}
	
	public static FilterList getEmpty(){
		return new FilterList();
	}
}
