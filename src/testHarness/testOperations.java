package testHarness;
import java.util.*;

import offerings.CourseOffering;
import systemUsers.AdminModel;
import systemUsers.InstructorModel;
import systemUsers.StudentModel;
import systemUsers.SystemUser;

public class testOperations {
	
	ArrayList<StudentModel> studentList = new ArrayList<StudentModel>();
	ArrayList<CourseOffering> courseList = new ArrayList<CourseOffering>();
	ArrayList<AdminModel> adminList = new ArrayList<AdminModel>();
	ArrayList<InstructorModel> instructorList = new ArrayList<InstructorModel>();
	static Scanner read = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		String userType = "";
		String command = "";
		AdminModel admin = null;
		StudentModel student = null;
		InstructorModel instructor = null;
		Boolean online = false;
		
		while(!online) {
			
			System.out.println("Enter user type: ");
			userType = read.next();
			
			if(userType.equals("admin")) {
				admin = adminLogin();
				System.out.println("System not currently online, would you like to start it? (y/n)");
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
			
			while(!command.equals("exit")) {
				
				if(userType.equals("admin")) {
					if(admin == null) admin = adminLogin();
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
						admin = null;
					}else if(command.equals("read file")) {
						String filename = read.next();
						System.out.println("Enter the filename: (ie. filename.txt)");
					}else {
						System.out.println("Invalid command");
					}
				
				} else if(userType.equals("instructor")) {
					instructor = instructorLogin();
					System.out.println("Here is a list of commands: \n"
							+ "");
					
				} else if(userType.equals("student")) {
					student = studentLogin();
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
			
			userType = "";
			System.out.println("Enter user type: ");
			userType = read.next();
			
			
		}
		
		read.close();
	}
	
	private static StudentModel studentLogin() {
		
		String firstName = "";
		String lastName = "";
		String ID = "";
		
		System.out.println("Enter first name: ");
		firstName = read.next();
		
		System.out.println("Enter last name: ");
		lastName = read.next();
		
		System.out.println("Enter ID number: ");
		ID = read.next();
		
		StudentModel student = new StudentModel();
		student.setID(ID);
		student.setName(firstName);
		student.setSurname(lastName);
		
		return student;
	}
	
	private static AdminModel adminLogin() {
		
		String firstName = "";
		String lastName = "";
		String ID = "";
		
		System.out.println("Enter first name: ");
		firstName = read.next();
		
		System.out.println("Enter last name: ");
		lastName = read.next();
		
		System.out.println("Enter ID number: ");
		ID = read.next();
		
		AdminModel admin = new AdminModel();
		admin.setID(ID);
		admin.setName(firstName);
		admin.setSurname(lastName);
		
		return admin;
	}
	
	private static InstructorModel instructorLogin() {
		
		String firstName = "";
		String lastName = "";
		String ID = "";
		
		System.out.println("Enter first name: ");
		firstName = read.next();
		
		System.out.println("Enter last name: ");
		lastName = read.next();
		
		System.out.println("Enter ID number: ");
		ID = read.next();
		
		InstructorModel instructor = new InstructorModel();
		instructor.setID(ID);
		instructor.setName(firstName);
		instructor.setSurname(lastName);
		
		return instructor;
	}
}
