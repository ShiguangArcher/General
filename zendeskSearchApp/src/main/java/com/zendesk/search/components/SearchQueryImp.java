package com.zendesk.search.components;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

import com.zendesk.search.interfaces.SearchQuery;
import com.zendesk.search.query.Attribute;
import com.zendesk.search.query.QueryType;

/**
 * Query for search
 * 
 * Format : 
 * 
 * 1) "groupName, _id"
 * 
 * 2) "groupName, attributeName, attributeValue"
 * 
 * @author shiguangli
 *
 */
public class SearchQueryImp implements SearchQuery{

	private QueryType queryType;
	
	private String groupName=null;;
	
	private StringTokenizer st;
	
	private List<String> parameters=new LinkedList<>();
	
	private Queue<Attribute> attributes=new LinkedList<>();
	
	//private LinkedHashMap<String, String> map;
	
	public SearchQueryImp() {
	}
	
	public SearchQueryImp(String query) {
		createQuery(query);
	}
	
	@Override
	public void createQuery(String query) {
		st=new StringTokenizer(query, ",");
		
		int count=st.countTokens();
		
		switch(count) {
			case 2 :
				queryType=QueryType.Group_ID;
				break;
			case 3 :
				queryType=QueryType.Group_Attribute_Value;
				break;
			default :
				queryType=QueryType.Multi_Attribute;
				break;
		}
		
		if(queryType==QueryType.Multi_Attribute) {
			while(st.hasMoreTokens()) {
				
				if(groupName==null) {
					groupName=st.nextToken();
					continue;
				}
				
				String attribute=st.nextToken();
				String value="";
				
				if(st.hasMoreTokens()) value=st.nextToken();
				
				Attribute att=new Attribute(attribute, value);
				
				attributes.add(att);
			}
		}
		else {
			while(st.hasMoreTokens()) parameters.add(st.nextToken());
		}
	}

	@Override
	public QueryType queryType() {
		
		return getQueryType();
	}
	
	@Override
	public String groupName() {
		return groupName;
	}

	@Override
	public List<String> getParameters() {
		return parameters;
	}
	
	public Queue<Attribute> getAttributes() {
		return attributes;
	}
	
	public QueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

}
