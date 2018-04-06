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
				System.out.println("System not currently online, would you like to start it? (y/n)");
				if(read.next().equals("y")) {
					System.out.println("System started successfully.");
					online = true;
					break;
				} else {
					System.out.println("Goodbye");
				}
			} else {
				System.out.println("System not currently online, please contact an administrator");
			}
		}
		
		while(online) {
			
			while(!command.equals("exit")) {
				
				if(user.equals("admin")) {
					System.out.println("Here is a list of commands: \n"
							+ "stop -> truns off the system and exits command line\n"
							+ "read file-> reads course file 'filename'\n"
							+ "exit -> returns to login screen while keeping system online");
					command = read.next();
					if(command.equals("stop")) {
						System.out.println("System shutting down. Goodbye.");
						online = false;
						break;
					}else if(command.equals("exit")) {
						System.out.println("Logging out. Goodbye.");
					}else if(command.equals("read file")) {
						String filename = read.next();
						System.out.println("Enter the filename: (ie. filename.txt)");
					}else {
						System.out.println("Invalid command");
					}
				
				} else if(user.equals("instructor")) {
					System.out.println("Here is a list of commands: \n"
							+ "");
					
				} else if(user.equals("student")) {
					System.out.println("Here is a list of commands: \n"
							+ "");
					
				} else {
					System.out.println("Invalid user type");
					break;
				}
			}
			
			if(!online) {
				break;
			}
			command = "";
			
			user = "";
			System.out.println("Enter user type: ");
			user = read.next();
			
		}
		
		read.close();
		
		
	}
}
