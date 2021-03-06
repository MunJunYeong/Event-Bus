/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Components.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class StudentMain {
	public static void main(String args[]) throws FileNotFoundException, IOException, NotBoundException, InterruptedException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** StudentMain(ID:" + componentId + ") is successfully registered. \n");

		StudentComponent studentsList = new StudentComponent("Students.txt");
		Event event = null;
		boolean done = false;
		while (!done) {
			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent();
				switch (event.getEventId()) {
				case ListStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeStudentList(studentsList)));
					break;
				case RegisterStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, registerStudent(studentsList, event.getMessage())));
					break;
				case DeleteStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, deleteStudent(studentsList, event.getMessage())));
					break;
				case checkStudentReservation:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.checkCourseReservation, checkGetCourse(studentsList, event.getMessage())));
					break;
				case QuitTheSystem:
					printLogEvent("Get", event);
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			}
		}
	}
	//Student id
	private static String checkGetCourse(StudentComponent studentsList, String message) {
		StringTokenizer stringTokenizer = new StringTokenizer(message);
		String studentId = stringTokenizer.nextToken();
		String courseId = stringTokenizer.nextToken();
		boolean flag = false;
		ArrayList<String> stuCourseList = null;
		for(int i=0; i< studentsList.vStudent.size(); i++) {
			if(studentsList.vStudent.get(i).studentId.equals(studentId)) {
				stuCourseList = studentsList.getCompletedCourseList(studentId);
				flag = true;
			}
		}
		if(!flag) {
			return "1"; //???????? ???????? ???? ???? 
		}
		String str = "";
		for(String list : stuCourseList) {
			str += list + " ";
		}
		return studentId+ " " +courseId + " " + str;
	}
	//delete
	private static String deleteStudent(StudentComponent studentsList, String message) {
		boolean flag = false;
		for(int i=0; i < studentsList.vStudent.size(); i++) {
			if(studentsList.vStudent.get(i).studentId.equals(message)) {
				studentsList.vStudent.remove(i);
				flag = true;
			}
		}
		if(flag) {
			return "This student is successfully deleted";
		}else {
			return "This student is not exist";
		}
	}
	//post
	private static String registerStudent(StudentComponent studentsList, String message) {
		Student  student = new Student(message);
		if (!studentsList.isRegisteredStudent(student.studentId)) {
			studentsList.vStudent.add(student);
			return "This student is successfully added.";
		} else
			return "This student is already registered.";
	}
	
	//get
	private static String makeStudentList(StudentComponent studentsList) {
		String returnString = "";
		if(studentsList.vStudent.size() == 0 ) {
			return "No StudentList";
		}
		for (int j = 0; j < studentsList.vStudent.size(); j++) {
			returnString += studentsList.getStudentList().get(j).getString() + "\n";
		}
		return returnString;
	}
	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
