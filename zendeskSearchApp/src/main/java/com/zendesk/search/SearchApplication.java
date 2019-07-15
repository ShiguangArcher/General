package com.zendesk.search;

import java.util.Scanner;

import com.zendesk.search.command.CommandHandler;
import com.zendesk.search.components.DataSourceImp;

/**
 * Zendesk Search Application
 * 
 * Functions
 * 1) Input files: users.json tickets.json organizations.json
 * 
 * 2) Load data
 * 
 * 3) Store data in memory
 * 
 * 4) Provide search interfaces
 * 
 * 5) Command line UI
 * 
 * @author shiguangli
 *
 */
public class SearchApplication {

	DataSourceImp datasource;
	CommandHandler cmdHandler;
	
	public void init() {
		datasource=new DataSourceImp();
		datasource.loadData();
		
		cmdHandler=new CommandHandler(datasource);
		
		start();
	}
	
	public void start() {
		
		// Start the UI to take user innput
		System.out.println("**************************************************");
		System.out.println("* Welcome to Zendesk Search !");
		System.out.println("* ");
		System.out.println("* Type 'help' for instruction");
		System.out.println("* Use formatted query to search in all data groups");
		System.out.println("* ");
		System.out.println("**************************************************");
		System.out.print(">> Please type here: ");
		
		processInput();
	}
	
	public void processInput() {
		
		Scanner scanner=new Scanner(System.in);
		try {
			boolean valid=true;
			
			while(valid) {
				String input="";
				
				input=scanner.nextLine();
				
				
				if(input.equals("")) return;
				
				cmdHandler.process(input);
				
				System.out.print("\n>> Please type query here: ");
			}
		}finally {
			scanner.close();
			System.out.println("\nThanks for using Zendesk Search !");
		}
		
	}
	
	public static void main(String args[]) {
		SearchApplication app=new SearchApplication();
		app.init();
	}
	
}
