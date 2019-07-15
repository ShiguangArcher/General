package com.zendesk.search.interfaces;

import java.util.Map;
import java.util.Set;

public interface SearchableGroup {
	
	public void addRecord(Map<String, Object> dataMap);
	
	public String getGroupName();
	
	public Set<String> getGroupAttributes(String groupName);
	
	public String search(String input);
	
	public String secondarySearh(String key, String value);
	
	public String chainSearch(SearchQuery query);
}
