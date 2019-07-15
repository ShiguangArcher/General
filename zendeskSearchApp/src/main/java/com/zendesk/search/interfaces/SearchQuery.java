package com.zendesk.search.interfaces;

import java.util.List;
import java.util.Queue;

import com.zendesk.search.query.Attribute;
import com.zendesk.search.query.QueryType;

public interface SearchQuery {
	
	public static final int queryDefaultType=-1;
	
	public void createQuery(String query);
	
	public QueryType queryType();
	
	public String groupName();
	
	public List<String> getParameters();
	
	public Queue<Attribute> getAttributes();
}
