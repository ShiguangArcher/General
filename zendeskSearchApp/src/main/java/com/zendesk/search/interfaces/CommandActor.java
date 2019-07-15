package com.zendesk.search.interfaces;

public interface CommandActor{
	
	public String doList(String command);
	
	public String doOther(String command);
}
