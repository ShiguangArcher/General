package com.zendesk.search.interfaces;

import java.util.Map;

public interface DataSource {
	
	public void loadData();
	
	public String doSearch(String query);
	
	public String doSearchByQuery(SearchQuery query);
	
	public Map<String, SearchableGroup> getLoadData();
}
