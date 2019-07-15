package com.zendesk.search.interfaces;

import java.util.Set;

public interface CorrelatedGroupRegistry {
	
	public void registerGroup(CorrelatedSearchableGroup group);
	
	public CorrelatedSearchableGroup findGroup(String groupName);
	
	public void addCorrelation(String groupName, Correlation correlation);
	
	public Set<String> getGroups();
}
