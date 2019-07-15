package com.zendesk.search.command;

import java.util.StringTokenizer;

import com.zendesk.search.interfaces.CommandActor;

public class CommandHandler {
	
	private final String HELP="help";
	private final String LIST="list";
	
	private CommandActor actor;
	
	public CommandHandler(CommandActor actor) {
		this.actor=actor;
	}
	
	public void process(String command) {
		
		if(command.trim().startsWith(HELP)) 
			onHelp();
		else if(command.startsWith(LIST)) 
			onList(command);
		else 
			onOthers(command);
	}
	
	public void onHelp() {
		System.out.println("************************************************************");
		System.out.println("* Zendesk Search Instruction");
		System.out.println("*");
		System.out.println("* Command:");
		System.out.println("*");
		System.out.println("*    help           - show the instruction");
		System.out.println("*");
		System.out.println("*    list           - list all the available groups");
		System.out.println("*");
		System.out.println("*    list groupName - list all the attributes of the group");
		System.out.println("*");
		System.out.println("* Query:");
		System.out.println("*");
		System.out.println("*    groupName, id");
		System.out.println("*    	- Example: users, 72");
		System.out.println("*");
		System.out.println("*    groupName, attribute, value, attribute, value.....");
		System.out.println("*    	- Here [attribute, value] can repeat many times");
		System.out.println("*       - Example: users, role, admin, verified, false");
		System.out.println("************************************************************");
	}
	
	public void onList(String command) {
		StringTokenizer st=new StringTokenizer(command);
		
		st.nextToken(); //Ignore word "list"
		
		String groupName="";
		if(st.hasMoreTokens()) {
			groupName=st.nextToken();
			System.out.println("\n*********** Attributes ***********\n");
		}
		else System.out.println("\n*********** Groups ***********\n");
		
		System.out.println(actor.doList(groupName));
	}
	
	public void onOthers(String command) {
		System.out.println("\n*********** Result ***********\n");
		System.out.println(actor.doOther(command));
	}
}
