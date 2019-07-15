package com.zendesk.search.components;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zendesk.search.error.ErrorMsg;
import com.zendesk.search.interfaces.CommandActor;
import com.zendesk.search.interfaces.CorrelatedGroupRegistry;
import com.zendesk.search.interfaces.CorrelatedSearchableGroup;
import com.zendesk.search.interfaces.Correlation;
import com.zendesk.search.interfaces.DataSource;
import com.zendesk.search.interfaces.SearchQuery;
import com.zendesk.search.interfaces.SearchableGroup;
import com.zendesk.search.query.CorrelatedAttribute;
import com.zendesk.search.query.QueryType;

/**
 * Data source
 * 
 * 1) Load JSON files as GROUPs into the CorrelatedGroup
 * 
 * 2) Provide registration for all the CorrelatedGroup and 
 *    can be used to search the CorrelatedGroup
 *    
 * 3) Conigure the Correlation between different CorrelatedGroup
 *    to provide the cross group data searching
 *    
 * 4) Search entry will foward the query to each CorrelatedGroup
 * 
 * 5) List Groups and Attributes of the group
 * 
 * @author shiguangli
 *
 */
public class DataSourceImp implements DataSource, CommandActor{
	
	private String dataPropertiesFile="config.properties";

	private CorrelatedGroupRegistry groupRegistry=new CorrelatedGroupRegistryImp();
	
	ObjectMapper mapper=new ObjectMapper();
	
	@Override
	public void loadData() {
		try {
			
			Properties prop = new Properties();
			
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(dataPropertiesFile);
			
		    prop.load(inputStream);
		    
		    // Load Groups
		    for(int i=1 ; ; i++) {
		    	String group=prop.getProperty("groupName_"+i);
				String file=prop.getProperty("groupFile_"+i);
				
				if(group==null || file==null) break;
				
				loadDataByGroup(group, file);
		    }
		    
		    // Load Correlation
		    for(int i=1 ; ; i++) {
		    	String corr=prop.getProperty("correlation_"+i);
				
				if(corr==null) break;
				
				StringTokenizer st=new StringTokenizer(corr, ",");
				
				if(st.countTokens()<6) continue;
				
				String groupName=st.nextToken();
				String correlatedGroupName=st.nextToken();
				String attribute=st.nextToken();
				String correlatedAttribute=st.nextToken();
				String targetAttribute=st.nextToken();
				String targetAlias=st.nextToken();
				
				addCorrelationByGroup(groupName, correlatedGroupName, attribute, correlatedAttribute, targetAttribute, targetAlias);
		    }
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadDataByGroup(String groupName, String filePath) {
		try {
			Map<String, Object>[] map=mapper.readValue(new File(filePath), new TypeReference<Map<String, Object>[]>() {});
			
			CorrelatedSearchableGroup group=new SearchableGroupImp(groupName, groupRegistry);
			
			for(Map<String, Object> m : map) group.addRecord(m);
			
			groupRegistry.registerGroup(group);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addCorrelationByGroup(String groupName, String correlatedGroupName, String attribute, String correlatedAttribute, String targetAttribute, String targetAlias) {
		Correlation correlation=groupRegistry.findGroup(groupName).getCorrelation();
		CorrelatedAttribute att=new CorrelatedAttribute(correlatedGroupName, attribute, correlatedAttribute, targetAttribute, targetAlias);
		
		correlation.addAttribute(att);
	}

	@Override
	public String doSearch(String query) {
		
		SearchQueryImp q=new SearchQueryImp(query);
		
		return doSearchByQuery(q);
	}
	
	@Override
	public String doSearchByQuery(SearchQuery query) {
		
		List<String> parameters=query.getParameters();
		
		if(query.queryType()==QueryType.Group_ID) 
			return doSearchByGroupName(parameters.get(0), parameters.get(1));
		else if(query.queryType()==QueryType.Group_Attribute_Value)
			return doSearchByGroupName(parameters.get(0), parameters.get(1), parameters.get(2));
		else {
			if(groupRegistry.findGroup(query.groupName())==null) {
				return ErrorMsg.GROUP_NOT_EXIST+query.groupName();
			}
			
			System.out.println("GroupName: "+query.groupName());
			return groupRegistry.findGroup(query.groupName()).chainSearch(query);
		}
	}

	private String doSearchByGroupName(String groupName, String id) {
		return groupRegistry.findGroup(groupName).search(id);
	}
	
	private String doSearchByGroupName(String groupName, String attribute, String value) {
		return groupRegistry.findGroup(groupName).secondarySearh(attribute, value);
	}
	
	@Override
	public Map<String, SearchableGroup> getLoadData() {
		
		return null;
	}

	@Override
	public String doList(String command) {
		StringBuilder builder=new StringBuilder();
		
		// command is empty - list all groups
		// command is a group - list all attributes
		
		if(command==null || command.equals("")) 
			groupRegistry.getGroups().forEach(group -> builder.append(group).append("\n"));
		else {
			if(groupRegistry.findGroup(command)==null) 
				return ErrorMsg.GROUP_NOT_EXIST+command;
			else
				groupRegistry.findGroup(command).getGroupAttributes(command).forEach(attribute -> builder.append(attribute).append("\n"));;
		}
		
		return builder.toString();
	}

	@Override
	public String doOther(String command) {
		return doSearch(command);
	}
}
