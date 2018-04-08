package testHarness;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import customDatatypes.EvaluationTypes;
import customDatatypes.Marks;
import customDatatypes.NotificationTypes;
import offerings.CourseOffering;
import offerings.ICourseOffering;
import offerings.OfferingFactory;
import systemUsers.AdminModel;
import systemUsers.InstructorModel;
import systemUsers.StudentModel;
import systemUsers.SystemUser;
import systemUsers.SystemUserModel;

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
		
	
		AdminModel defaultAdmin = new AdminModel();
		defaultAdmin.setName("default");
		defaultAdmin.setSurname("admin");
		defaultAdmin.setID("1");
		adminList.add(defaultAdmin);
			
		while(!online) {
			
			System.out.println("Enter user type: ");
			userType = read.next();
			read.nextLine();
			if(userType.equalsIgnoreCase("admin")) {
				admin = (AdminModel) authenticate("admin");

				if(admin == null) {
					System.out.println("You are not an authorized administrator");
				}
				else {
									
					System.out.println("System not currently online, would you like to start it? (y/n)");
					if(read.next().equalsIgnoreCase("y")) {
						System.out.println("System started successfully.");
						online = true;
						read.nextLine();
						break;
					} else {
						System.out.println("Goodbye");
					}
				}
				
				
			}
			else {
				System.out.println("System is currently offline. Contact an administrator.");
			}
		}
		
		while(online) {
			
			while(!command.equals("exit")) {
				
				if(userType.equals("admin")) {
					System.out.println("Here is a list of commands: \n"
							+ "stop -> turns off the system and exits command line\n"
							+ "read file-> reads course file 'filename'\n"
							+ "exit -> returns to login screen while keeping system online\n"
							+ "enroll student -> add a student to a class list");
					
					command = read.nextLine();
					
					switch(command) {
					
					case "stop":
						
						System.out.println("System shutting down. Goodbye.");
						online = false;
						command = "exit";
						admin = null;
						break;
						
					case "read file":
						System.out.println("Enter the filename: (ie. filename.txt)");
						String filename = read.next();
						read.nextLine();
						try {
						// Create an instance of an OfferingFactory
						OfferingFactory factory = new OfferingFactory();
						BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
						// Use the factory to populate as many instances of courses as many files we've got.
						CourseOffering	courseOffering = factory.createCourseOffering(br);
						courseList.add(courseOffering);
						br.close();
						break;
						}
						catch(Exception e) {
							System.out.println("Invalid file");
							break;
						}
					case "exit":
						
						System.out.println("Logging out. Goodbye.");
						admin = null;
						break;
						
					case "enroll student":
						
						StudentModel studentEnroll = null;
						CourseOffering course = null;
						String ID = read.next();
						
						System.out.println("Enter the ID number of the student you wish to enroll: ");
						
						
						for(StudentModel stu : studentList) {
							if(stu.getID() == ID) studentEnroll = stu; 
						}
						
						if(studentEnroll == null) {
							System.out.print("Student not found");
						} else {
							
							System.out.println("Enter course ID to enroll student in");
							ID = read.next();
							
							for(CourseOffering off : courseList) {
								if(off.getCourseID() == ID) course = off;
							}
							
							if(course == null) {
								System.out.println("Not a valid course code");
							} else {
								List<ICourseOffering> courses = studentEnroll.getCoursesEnrolled();
								courses.add(course);
								studentEnroll.setCoursesEnrolled(courses);
								System.out.println("Student enrolled in course");
							}
						}
						break;
						
					default:
						
						System.out.println("Invalid command"); 
						break;
					}
				
				} else if(userType.equals("instructor")) {
					
					System.out.println("Here is a list of commands: \n"
							+ "add -> add a mark for a student in a class you tutor\n"
							+ "modify -> modify a mark for a student in a class you tutor\n"
							+ "final -> calculate the final mark for a student in a class you tutor\n"
							+ "print -> print the class record for a class you tutor"
 							+ "exit -> returns to login screen while keeping system online");
						
											
							boolean done;
							command = read.next();
							read.nextLine();
							switch(command) {
											
							case "add":
							case "modify":
								done = false;
								while (!done) {
									
									List<ICourseOffering> courses = instructor.getIsTutorOf();

									for(int c = 0; c < courses.size(); c++){
										System.out.println(courses.get(c).getCourseName() + " " + courses.get(c).getCourseID() + "\n");
									}
									
									if(command.equals("add")){
										System.out.println("\nEnter the ID for the course you'd like to add a students mark, or Enter to go back: ");
									}else{
										System.out.println("\nEnter the ID for the course you'd like to modify a students mark, or Enter to go back: ");
									}
									String course_id = read.nextLine();

									if (course_id.equals(""))
										break;
																		
									boolean isTutor = false;
									ICourseOffering course = null;
									for(int q = 0; q <courses.size(); q++){
										if(courses.get(q).getCourseID() == course_id){
											isTutor = true;
											course = courses.get(q);
										}	
									}
									if(isTutor == true){
										
										if(command.equals("add")){
											System.out.println("Enter the ID for the student you would like to add a mark for: ");
										}else{
											System.out.println("Enter the ID for the student you would like to modify a mark for: ");
										}
										String student_id = read.next();	
										
										List<StudentModel> students = course.getStudentsEnrolled();
										boolean isStudent = false;
										StudentModel studentInClass = null;

										for(int s = 0; s <students.size(); s++){
											if(students.get(s).getID().equals(student_id)){
												isStudent = true;
												studentInClass = students.get(s);
											}	
										}
										if(isStudent == true){
											
											Map<ICourseOffering, EvaluationTypes> evaluationList = studentInClass.getEvaluationEntities();
											EvaluationTypes eTypes = evaluationList.get(course);
											
											Map<ICourseOffering, Marks> marks = studentInClass.getPerCourseMarks();
											Marks classMarks = marks.get(course);
											
											String typeString = eTypes.getText();
											System.out.println("Please input the evaluation entity:\n" + typeString);
											String evaluationChanged = read.next();
											
											System.out.println("Please input the mark for the evaluation entity: ");
											String markString = read.next();
											double evaluationMark = Double.parseDouble(markString);
											
											classMarks.addToEvalStrategy(evaluationChanged, evaluationMark);
											
											
											if(studentInClass.getNotificationType().equals(NotificationTypes.EMAIL)){
												System.out.println("The student has been notified via email that their mark has been changed for " + course.getCourseName() + ".");
											}else if(studentInClass.getNotificationType().equals(NotificationTypes.CELLPHONE)){
												System.out.println("The student has been notified via cellphone that their mark has been changed for " + course.getCourseName() + ".");
											}else if(studentInClass.getNotificationType().equals(NotificationTypes.PIGEON_POST)){
												System.out.println("The student has been notified via pigeon post that their mark has been changed for " + course.getCourseName() + ".");
											}
											
										} else{
											System.out.println("Invalid student ID inputted.\n");
											done = true;
											break;
										}
										
									} else{
										System.out.println("Invalid course ID inputted.\n");
										done = true;
										break;
									}
																		
								}
								break;



							case "final":
								done = false;
								while (!done) {
									
									List<ICourseOffering> courses = instructor.getIsTutorOf();
									
									for(int c = 0; c < courses.size(); c++){
										System.out.println(courses.get(c).getCourseName() + " " + courses.get(c).getCourseID() + "\n");
									}

									System.out.println("Enter the ID for the course you'd like to calculate a student's final mark, or Enter to go back: ");

									String course_id = read.nextLine();

									if (course_id.equals(""))
										break;
																		
									boolean isTutor = false;
									ICourseOffering course = null;
									for(int q = 0; q <courses.size(); q++){
										if(courses.get(q).getCourseID() == course_id){
											isTutor = true;
											course = courses.get(q);
										}	
									}
									if(isTutor == true){
										
										System.out.println("Enter the ID of the student whose final mark you want to calculate: ");

										String student_id = read.next();	
										
										List<StudentModel> students = course.getStudentsEnrolled();
										boolean isStudent = false;
										StudentModel studentInClass = null;

										for(int s = 0; s <students.size(); s++){
											if(students.get(s).getID() == student_id){
												isStudent = true;
												studentInClass = students.get(s);
											}	
										}
										if(isStudent == true){
											
											CourseOffering courseReal = (CourseOffering) course;
											courseReal.calculateFinalGrade(studentInClass.getID());
											
											//Confirmation statement of final grade calculated.
											
										} else{
											System.out.println("Invalid student ID inputted.\n");
											done = true;
											break;
										}
										
									} else{
										System.out.println("Invalid course ID inputted.\n");
										done = true;
										break;
									}
									
									
								}
								break;

								
							case "print":
								done = false;
								while (!done) {
									
									List<ICourseOffering> courses = instructor.getIsTutorOf();
									
									for(int c = 0; c < courses.size(); c++){
										System.out.println(courses.get(c).getCourseName() + " " + courses.get(c).getCourseID() + "\n");
									}

									System.out.println("Enter the ID for the course you'd like to print records for, or Enter to go back: ");

									String course_id = read.next();

									if (course_id.equals(""))
										break;
																		
									boolean isTutor = false;
									ICourseOffering course = null;
									for(int q = 0; q <courses.size(); q++){
										if(courses.get(q).getCourseID() == course_id){
											isTutor = true;
											course = courses.get(q);
										}	
									}
									if(isTutor == true){
										
										System.out.println("To print records for all students input 'all', or 'choose' for individual records: ");
										String recordType = read.next();

										if(recordType.equals("choose")){
											
											String loopID = null;
											
											while(loopID.equals("exit")){
											
												System.out.println("Enter the ID for the student you would like to print records for, or 'exit' to finish: ");
		
												String student_id = read.next();	
												
												List<StudentModel> students = course.getStudentsEnrolled();
												boolean isStudent = false;
												StudentModel studentInClass = null;
		
												for(int s = 0; s <students.size(); s++){
													if(students.get(s).getID() == student_id){
														isStudent = true;
														studentInClass = students.get(s);
													}	
												}
												if(isStudent == true){
													
													CourseOffering courseReal = (CourseOffering) course;
													courseReal.calculateFinalGrade(studentInClass.getID());
													
													int studentInClassID = Integer.parseInt(studentInClass.getID());
													StudentModel studentLoop = students.get(studentInClassID);
													Map<ICourseOffering, Marks> studentMarksMap = studentLoop.getPerCourseMarks();
													Marks studentMarks = studentMarksMap.get(course);
													studentMarks.initializeIterator();
													
													String marksIter = null;
													
													while(studentMarks.hasNext()){
														marksIter = marksIter + " " + studentMarks.getCurrentKey() + " : " + studentMarks.getCurrentValue() + " ;";
													}
													
													System.out.println("Name: " + studentLoop.getName() + " " + studentLoop.getSurname() + ", ID: " + studentLoop.getID() + "\n   Course Marks: " + marksIter);
													
													
													//Confirmation statement of final grade calculated.
													
												} else{
													System.out.println("Invalid student ID inputted.\n");
													done = true;
													break;
												}
											}
										} else if(recordType.equals("all")){
											
											CourseOffering courseReal = (CourseOffering) course;
											courseReal.calculateFinalGrades();
											
											List<StudentModel> students = course.getStudentsEnrolled();
											for(int s = 0; s <students.size(); s++){
												
												StudentModel studentLoop = students.get(s);
												Map<ICourseOffering, Marks> studentMarksMap = studentLoop.getPerCourseMarks();
												Marks studentMarks = studentMarksMap.get(course);
												studentMarks.initializeIterator();
												
												String marksIter = null;
												
												while(studentMarks.hasNext()){
													marksIter = marksIter + " " + studentMarks.getCurrentKey() + " : " + studentMarks.getCurrentValue() + " ;";
												}
												
												System.out.println(s+1 + " - Name: " + studentLoop.getName() + " " + studentLoop.getSurname() + " " + studentLoop.getID() + "\n   Course Marks: " + marksIter);
												
											}
										} else{
											System.out.println("Invalid option inputted.\n");
											done = true;
											break;											
										}
										
									} else{
										System.out.println("Invalid course ID inputted.\n");
										done = true;
										break;
									}
									
								}
								break;


								
							case "exit":

								System.out.println("Logging out. Goodbye.");
						 		instructor = null;
						 		break;
						 		
							default:
								System.out.println("Invalid command");
								break;
						
						
						
						
							
		
						//101 to 395
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
							String course_id = read.nextLine();
							
							if (course_id.equals("")) // go back to list of commands
								break;
							
							// find the course in the courseList
							for (CourseOffering course_item : courseList) {
								if (course_item.getCourseID().equalsIgnoreCase(course_id)) {
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
							String course_id = read.nextLine();
							
							if (course_id.equals("")) // go back to list of commands
								break;
							
							// find the course in the courseList
							for (CourseOffering course_item : courseList) {
								if (course_item.getCourseID().equalsIgnoreCase(course_id)) {
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
			read.nextLine();
			if(userType.equals("admin"))
				admin = (AdminModel) authenticate(userType);
			else if(userType.equals("instructor"))
				instructor = (InstructorModel) authenticate(userType);
			else if(userType.equals("student"))
				student = (StudentModel) authenticate(userType);
			else {
				System.out.println("Invalid user type");
			}
			if(userType.equals("admin") || userType.equals("instructor") || userType.equals("student"))
				if(admin == null && student == null && instructor == null) {
					System.out.println("User not on system");
					command = "exit";
				}
			
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
	
	private static SystemUserModel authenticate(String user) {
		AdminModel admin = null;
		InstructorModel instructor = null;
		StudentModel student = null;
		
		if(user.equals("admin")) {
			admin = adminLogin();
			for(AdminModel existingAdmin : adminList) {
				if(existingAdmin.getID().equalsIgnoreCase(admin.getID()))
					if(existingAdmin.getName().equalsIgnoreCase(admin.getName()))
						if(existingAdmin.getSurname().equalsIgnoreCase(admin.getSurname())) {
								admin = existingAdmin;
								return admin;
						}
			}
			return null;
		}else if(user.equals("instructor")) {
			instructor = instructorLogin();
			for(InstructorModel existingTutor : instructorList) {
				if(existingTutor.getID().equalsIgnoreCase(instructor.getID()))
					if(existingTutor.getName().equalsIgnoreCase(instructor.getName()))
						if(existingTutor.getSurname().equalsIgnoreCase(instructor.getSurname())) {
								instructor = existingTutor;
								return instructor;
						}
			}
			return null;
		}else if(user.equals("student")) {
			student = studentLogin();
			for(StudentModel existingStudent : studentList) {
				if(existingStudent.getID().equalsIgnoreCase(student.getID()))
					if(existingStudent.getName().equalsIgnoreCase(student.getName()))
						if(existingStudent.getSurname().equalsIgnoreCase(student.getSurname())) {
								student = existingStudent;
								return student;
						}
			}
			return null;
		}else {
			return null;
		}
	}
		
}
