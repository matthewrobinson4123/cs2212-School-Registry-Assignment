package testHarness;
import java.util.*;

public class testOperations {
	
	public static void main(String[] args) {
		
		Scanner read = new Scanner(System.in);
		String user = "";
		String command = "";
		Boolean online = false;
		
		while(!online) {
			
			System.out.println("Enter user type: ");
			user = read.next();
			
			if(user.equals("admin")) {
				System.out.println("System not currently online, would you like to start it? (y/N)");
				if(read.next().equals("y")) {
					System.out.println("System started successfully.");
					online = true;
				} else {
					System.out.println("Goodbye");
				}
			} else {
				System.out.println("System not currently online, please contact an administrator");
			}
		}
		
		while(online) {
			
			if(user.equals("admin")) {
				System.out.println("Here is a list of commands: \n"
						+ "stop -> truns off the system and exits command line\n"
						+ "read filename.txt -> reads course file 'filename'\n"
						+ "exit -> returns to login screen while keeping system online");
				while(!command.equals("exit")) {
					command = read.next();
				}
				user = "";
				
			} else {
				System.out.println("Enter user type: ");
				user = read.next();
				
				if(user.equals("instructor")) {
					System.out.println("Here is a list of commands: \n"
							+ "");
					
				} else if(user.equals("student")) {
					System.out.println("Here is a list of commands: \n"
							+ "");
					
				} else {
					System.out.println("Invalid user type");
				}
			}
		}
		
		read.close();
		
	}
}
