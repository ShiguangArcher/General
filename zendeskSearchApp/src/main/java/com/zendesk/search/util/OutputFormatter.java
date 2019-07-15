package com.zendesk.search.util;

import java.util.Map;

public class OutputFormatter {
	
	private int defaultAttributWidth=30;
	
	public OutputFormatter() {
		
	}
	
	// Key and value column format
	public String format(String key, String value) {
		return String.format("%-"+defaultAttributWidth+"s %s", key, value);
	}
	
	public String formatMap(Map m) {
		if(m==null) return "";
		
		StringBuilder sb=new StringBuilder();
		
		for(Object k : m.keySet()) {
			String key=String.valueOf(k);
			String value=String.valueOf(m.get(k));
			
			sb.append(format(key, value)).append("\n");
		}
		
		return sb.toString();
	}
}
