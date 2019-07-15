package com.zendesk.search.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.zendesk.search.error.ErrorMsg;
import com.zendesk.search.interfaces.CorrelatedGroupRegistry;
import com.zendesk.search.interfaces.CorrelatedSearchableGroup;
import com.zendesk.search.interfaces.Correlation;
import com.zendesk.search.interfaces.SearchQuery;
import com.zendesk.search.query.Attribute;
import com.zendesk.search.query.CorrelatedAttribute;
import com.zendesk.search.util.OutputFormatter;

/**
 * SearchableGroup
 * 
 * 	1) Store loaded JSON file as _id based data map 
 * 
 * 	2) Store correlations to othere groups
 * 
 *  3) Search functions 
 *  	- id search
 *  	- one attribute search
 *  	- multiple attributes search
 *  
 *  4) Provide correlated search results to correlated groups
 * 
 * @author shiguangli
 *
 */
public class SearchableGroupImp implements CorrelatedSearchableGroup{

	private final String default_search_key="_id";
	private final String groupName;
	
	private CorrelatedGroupRegistry registry;
	private Correlation correlation=new CorrelationImp();
	
	private OutputFormatter formatter=new OutputFormatter();
	
	// Key - _id : value - Map<Attribute, value>
	private Map<String, Map<String, Object>> groupData=new HashMap<>();
	private Set<String> attributes=new HashSet<>();

	public SearchableGroupImp(String groupName) {
		this.groupName=groupName;
	}
	
	public SearchableGroupImp(String groupName, CorrelatedGroupRegistry registry) {
		this(groupName);
		this.registry=registry;
	}
	
	@Override
	public String getGroupName() {
		return groupName;
	}
	
	@Override
	public void addRecord(Map<String, Object> dataMap) {
		
		groupData.put(String.valueOf(dataMap.get(default_search_key)), dataMap);
		attributes.addAll(dataMap.keySet());
	}

	@Override
	public String search(String id) {
		if(id==null || id.trim().equals("")) return null;
		
		StringBuilder builder=new StringBuilder();
		id=id.trim();
		
		if(!groupData.containsKey(id)) {
			return ErrorMsg.ATTRIBUTE_NOT_EXIST+id;
		}
		
		builder.append(formatter.formatMap(groupData.get(id)));
		
		for(CorrelatedAttribute attribute : correlation.getCorrelations()) {
			String correlatedValue=String.valueOf(groupData.get(id).get(attribute.getAttribute()));
			
			builder.append(doCorreleatedSearch(attribute, attribute.getCorrelatedAttribute(), correlatedValue));
		}
		
		builder.append("\n\n");
		
		return builder.toString();
	}

	@Override
	public String secondarySearh(String key, String value) {
		Set<String> idSet=new HashSet<>();
		String valFound="";
		
		for(String id : groupData.keySet()) {
			Map<String, Object> attributes=groupData.get(id);

			valFound=String.valueOf(attributes.get(key.trim()));
			if(valFound.equals(value.trim())) idSet.add(id);
		}
		
		StringBuilder builder=new StringBuilder();
		
		if(idSet.isEmpty()) {
			builder.append(ErrorMsg.ATTRIBUTE_NOT_EXIST+key+" "+value);
		}
		else {
			for(String id : idSet) builder.append(search(id));
		}
		
		return builder.toString();		
	}
	
	@Override
	public String chainSearch(SearchQuery query) {
		
		Queue<Attribute> attributes=query.getAttributes();
		
		Set<String> ids=groupData.keySet();
		
		String attribute="";
		String value="";
		
		while(!attributes.isEmpty() && ids!=null) {
			Attribute a=attributes.poll();
			
			attribute=a.getAttribute();
			value=a.getValue();
			
			ids=searchNextAttribute(ids, attribute, value);
		}
		
		if(ids==null || ids.isEmpty()) return ErrorMsg.ATTRIBUTE_NOT_EXIST+attribute;
		
		StringBuilder builder=new StringBuilder();
		
		for(String id : ids) builder.append(search(id));
		
		return builder.toString();
	}
	
	private Set<String> searchNextAttribute(Set<String> idsFromLastSearch, String key, String value) {

		if(idsFromLastSearch==null || idsFromLastSearch.isEmpty()) return null;
		
		Set<String> result=new HashSet<>();
		
		for(String id : idsFromLastSearch) {
			Map<String, Object> attributes=groupData.get(id);
			
			String v=String.valueOf(attributes.get(key.trim()));
			if(v.equals(value.trim())) result.add(id);
		}
		
		return result;
	}
	
	public Set<String> alternativeAttributeSearh(String key, String value, String targetKey) {
		Set<String> result=new HashSet<>();
		
		for(String id : groupData.keySet()) {
			Map<String, Object> attributes=groupData.get(id);
			
			String v=String.valueOf(attributes.get(key.trim()));
			
			if(v.equals(value.trim())) result.add(String.valueOf(attributes.get(targetKey)));
		}
		
		return result;		
	}
	
	public String doCorreleatedSearch(CorrelatedAttribute att, String key, String value) {
		StringBuilder builder=new StringBuilder();

		CorrelatedSearchableGroup correlatedGroup=registry.findGroup(att.getCorrelatedGroupName());

		if(correlatedGroup==null) return "";

		Set<String> result=correlatedGroup.alternativeAttributeSearh(key, value, att.getTargetAttribute());

		int i=1;

		for(String r : result) builder.append(formatter.format(att.getTargetAlias()+"_"+i++, r)).append("\n");

		return builder.toString();
	}

	@Override
	public void addCorrelation(Correlation correlation) {
		this.correlation=correlation;
	}

	@Override
	public Correlation getCorrelation() {
		return correlation;
	}

	@Override
	public Set<String> getGroupAttributes(String groupName) {
		return attributes;
	}
}
