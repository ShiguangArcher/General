package com.zendesk.search.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.zendesk.search.interfaces.CorrelatedGroupRegistry;
import com.zendesk.search.interfaces.CorrelatedSearchableGroup;
import com.zendesk.search.interfaces.Correlation;

public class CorrelatedGroupRegistryImp implements CorrelatedGroupRegistry{

	private Map<String, CorrelatedSearchableGroup> map=new HashMap<>();
	
	@Override
	public void registerGroup(CorrelatedSearchableGroup group) {
		map.put(group.getGroupName(), group);
		
	}

	@Override
	public CorrelatedSearchableGroup findGroup(String groupName) {
		return map.get(groupName);
	}

	@Override
	public void addCorrelation(String groupName, Correlation correlation) {
		map.get(groupName).addCorrelation(correlation);
	}

	@Override
	public Set<String> getGroups() {
		return map.keySet();
	}
	
}
