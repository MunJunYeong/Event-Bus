/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */
package Components.Course;

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

public class CourseMain {
	public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("CourseMain (ID:" + componentId + ") is successfully registered...");

		CourseComponent coursesList = new CourseComponent("Courses.txt");
		Event event = null;
		boolean done = false;
		while (!done) {
			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent();
				switch (event.getEventId()) {
				case ListCourses:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeCourseList(coursesList)));
				case RegisterCourses :
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, registerCourse(coursesList, event.getMessage())));
					
				case DeleteCourses :
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, deleteCourse(coursesList, event.getMessage())));
					
				case checkCourseReservation :
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.RegisterReservation, checkgetCourse(coursesList, event.getMessage())));
					break;
					
				case QuitTheSystem:
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			}
		}
	}
	private static String checkgetCourse(CourseComponent coursesList, String message) {
		if(message.equals("1")) {
			return "1";
		}
		boolean existCourseId = false;
		StringTokenizer stringTokenizer = new StringTokenizer(message);
		String studentId = stringTokenizer.nextToken();
		String courseId = stringTokenizer.nextToken();
		ArrayList<String> studentCompletedCourse = new ArrayList<String>();
		while (stringTokenizer.hasMoreTokens()) {
			studentCompletedCourse.add(stringTokenizer.nextToken());
		}
		
		ArrayList<String> completedCourseList =null;
		for(int i=0; i<coursesList.vCourse.size(); i++) {
			if(coursesList.vCourse.get(i).courseId.equals(courseId)) {
				completedCourseList = coursesList.getCompletedCourseList(courseId);
				existCourseId = true;
			}
		}
		if(!existCourseId) {
			return "2";
		}
		boolean rFlag =false;
		
		if(completedCourseList.size() == 0) {
			rFlag=true;
		}else {
			for(int i = 0; i < completedCourseList.size(); i++) {
				for(int j=0; j<studentCompletedCourse.size(); j++) {
					if(completedCourseList.get(i).contains(studentCompletedCourse.get(j))) {
						rFlag=true;
					}
				}
			}
		}
		if(rFlag) {
			return studentId+ " " +courseId;
		}else {
			return "3";
		}
	}
	private static String deleteCourse(CourseComponent coursesList, String message) {
		boolean flag = false;
		for(int i=0; i < coursesList.vCourse.size(); i++) {
			if(coursesList.vCourse.get(i).courseId.equals(message)) {
				coursesList.vCourse.remove(i);
				flag = true;
			}
		}
		if(flag) {
			return "This course is successfully deleted";
		}else {
			return "This course is not exist";
		}
	}
	private static String registerCourse(CourseComponent coursesList, String message) {
		Course course = new Course(message);
		if (!coursesList.isRegisteredCourse(course.courseId)) {
			coursesList.vCourse.add(course);
			return "This course is successfully added.";
		} else
			return "This course is already registered.";
	}
	private static String makeCourseList(CourseComponent coursesList) {
		String returnString = "";
		if(coursesList.vCourse.size() == 0 ) {
			return "No CourseList";
		}
		for (int j = 0; j < coursesList.vCourse.size(); j++) {
			returnString += coursesList.getCourseList().get(j).getString() + "\n";
		}
		return returnString;
	}
	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
