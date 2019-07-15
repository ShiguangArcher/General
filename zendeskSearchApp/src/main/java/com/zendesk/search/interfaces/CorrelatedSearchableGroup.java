package com.zendesk.search.interfaces;

import java.util.Set;

public interface CorrelatedSearchableGroup extends SearchableGroup{
	
	public void addCorrelation(Correlation correlation);
	
	public Correlation getCorrelation();
	
	public Set<String> alternativeAttributeSearh(String key, String value, String targetKey); 
}
