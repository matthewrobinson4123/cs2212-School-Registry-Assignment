package testHarness;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import customDatatypes.NotificationTypes;
import offerings.CourseOffering;
import offerings.OfferingFactory;
import systemUsers.AdminModel;
import systemUsers.InstructorModel;
import systemUsers.StudentModel;
import systemUsers.SystemUser;

public class testOperations {
	
	private static ArrayList<StudentModel> studentList = new ArrayList<StudentModel>();
	private static ArrayList<CourseOffering> courseList = new ArrayList<CourseOffering>();
	private static ArrayList<AdminModel> adminList = new ArrayList<AdminModel>();
	private static ArrayList<InstructorModel> instructorList = new ArrayList<InstructorModel>();
	static Scanner read = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		
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
					System.out.println("Here is a list of commands: \n"
							+ "stop -> truns off the system and exits command line\n"
							+ "read file-> reads course file 'filename'\n"
							+ "exit -> returns to login screen while keeping system online");
					command = read.next();
					
					switch(command) {
					
					case "stop":
						
						System.out.println("System shutting down. Goodbye.");
						online = false;
						break;
						
					case "read file":
						
						String filename = read.next();
						System.out.println("Enter the filename: (ie. filename.txt)");
						
						// Create an instance of an OfferingFactory
						OfferingFactory factory = new OfferingFactory();
						BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
						// Use the factory to populate as many instances of courses as many files we've got.
						CourseOffering	courseOffering = factory.createCourseOffering(br);
						courseList.add(courseOffering);
						br.close();
						break;
						
					case "exit":
						
						System.out.println("Logging out. Goodbye.");
						admin = null;
						break;
						
					default:
						
						System.out.println("Invalid command"); 
						break;
					}
				
				} else if(userType.equals("instructor")) {
					System.out.println("Here is a list of commands: \n"
							+ "exit -> returns to login screen while keeping system online");
					if(command.equals("exit")) {
						System.out.println("Logging out. Goodbye.");
						instructor = null;
					}
					else {
						System.out.println("Invalid command");
					}
				} else if(userType.equals("student")) {
					
					System.out.println("Here is a list of commands: \n"
							+ "enroll -> enroll in a course\n"
							+ "notify -> change notification preferences\n"
							+ "print -> print the record for a course you are attending\n"
							+ "exit -> returns to login screen while keeping system online");
					
					boolean done;
					command = read.next();
					read.nextLine();
					switch(command) {
					
					case "enroll":
						done = false;
						while (!done) {
							System.out.println("Enter the ID for the course you'd like to enroll in or Enter to go back: ");
							String course_id = read.next();
							
							if (course_id.equals("")) // go back to list of commands
								break;
							
							// find the course in the courseList
							for (CourseOffering course_item : courseList) {
								if (course_item.getCourseID().equals(course_id)) {
									done = true;
									// check if student can take the course then modify both relevant lists
									if (student.getCoursesAllowed().contains(course_item) 
											&& course_item.getStudentsAllowedToEnroll().contains(student)) {
										student.getCoursesEnrolled().add(course_item);
										course_item.getStudentsEnrolled().add(student);
									} else {
										done = false;
									}
									break;
								}
							}
							if (!done) // sloppy
								System.out.println("Invalid course ID, or you cannot enroll in that course.");
						}
						break;
						
					case "notify":
						done = false;
						while (!done) {
							
							System.out.println("Enter your preferred notification type (email, cellphone, pigeon post)"
									+ "or Enter to exit: ");
							
	
							String notification_type = read.nextLine();
							switch(notification_type) {
							
								case "email":
									student.setNotificationType(NotificationTypes.EMAIL); 
									break;
								
								case "cellphone":
									student.setNotificationType(NotificationTypes.CELLPHONE); 
									break;
								
								case "pigeon post":
									student.setNotificationType(NotificationTypes.PIGEON_POST); 
									break;
								
								case "":
									System.out.println("Exiting notification settings");
									done = true; 
									break;
									
								default:
									System.out.println("Invalid type."); 
									break;
							}
							
							
						}
						break;
						
					case "print":
						done = false;
						while (!done) {
							System.out.println("Enter the ID for the course you'd like to print or Enter to go back: ");
							String course_id = read.next();
							
							if (course_id.equals("")) // go back to list of commands
								break;
							
							// find the course in the courseList
							for (CourseOffering course_item : courseList) {
								if (course_item.getCourseID().equals(course_id)) {
									done = true;
									// check if student is in the course
									if (student.getCoursesEnrolled().contains(course_item) 
											&& course_item.getStudentsEnrolled().contains(student)) {
										System.out.println(course_item.getCourseName());
										System.out.println(course_item.getCourseID());
										System.out.println("Semester " + Integer.toString(course_item.getSemester()));
										System.out.println("Instructor(s):\n" + course_item.getInstructor());
										System.out.println("Evaluation:\n" + course_item.getEvaluationStrategies());
									} else {
										done = false;
									}
									break;
								}
							}
							if (!done) // sloppy
								System.out.println("Invalid course ID, or you are not enrolled in that course.");
						}
						break;
						
					case "exit":
						System.out.println("Logging out. Goodbye.");
						student = null;
						break;
						
					default:
						System.out.println("Invalid command"); 
						break;
					}
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
			
			if(userType.equals("admin"))
				admin = adminLogin();
			else if(userType.equals("instructor"))
				instructor = instructorLogin();
			else if(userType.equals("student"))
				student = studentLogin();
			
			
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
