package Components.Reservation;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Components.Course.CourseComponent;
import Components.Student.StudentComponent;
import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class ReservationMain {
	public static void main(String[] args) throws NotBoundException, IOException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** ReservationMain(ID:" + componentId + ") is successfully registered. \n");

		ReservationComponent reservationList = new ReservationComponent("Reservations.txt");
		StudentComponent studentsList = new StudentComponent("Students.txt");
		CourseComponent coursesList = new CourseComponent("Courses.txt");
		
		Event event = null;
		boolean done = false;
		while(!done) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent();
				switch (event.getEventId()) {
				case ListReservations :
					printLogEvent("Get", event);
					System.out.println(reservationList);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeReservationList(reservationList)));
					break;
				case RegisterReservation :
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, registerReservation(reservationList, studentsList, coursesList, event.getMessage())));
					break;
				default : 
					break;
				}
			}
		}
		
	}
	
	
	@SuppressWarnings("unlikely-arg-type")
	private static String registerReservation(ReservationComponent reservationList, StudentComponent studentsList, CourseComponent coursesList, String message) {
		boolean sFlag = false;
		boolean cFlag = false;
		boolean rFlag = false;
		String reservation = message.toString();
		Reservation res = new Reservation(message);
		StringTokenizer stringTokenizer = new StringTokenizer(reservation);
		String studentId = stringTokenizer.nextToken();
		String courseId = stringTokenizer.nextToken();
		//학생이 존재하는지
		for(int i=0; i< studentsList.vStudent.size(); i++) {
			if(studentsList.vStudent.get(i).studentId.equals(studentId)) {
				sFlag = true;
			}
		}
		//과목이 존재하는지
		for(int i=0; i< coursesList.vCourse.size(); i++) {
			if(coursesList.vCourse.get(i).courseId.equals(courseId)) {
				cFlag = true;
			}
		}
		//선수과목 이수했는지
		ArrayList<String> stuCourseList = studentsList.getCompletedCourseList(studentId);
		ArrayList<String> couCourseList = coursesList.getCompletedCourseList(courseId);
		
		if(couCourseList == null) {
			rFlag=true;
		}else {
			for(int i = 0; i < couCourseList.size(); i++) {
				for(int j=0; j<stuCourseList.size(); j++) {
					if(couCourseList.get(i).contains(stuCourseList.get(j))) {
						rFlag=true;
					}
				}
			}
		}
		if(sFlag && cFlag && rFlag) {
			reservationList.vReservation.add(res);
			return "This Reservation is successfully Registered";
		}else {
			return "Reservation register failed";
		}
	}
	private static String makeReservationList(ReservationComponent reservationList) {
		String returnString = "";
		for (int j = 0; j < reservationList.vReservation.size(); j++) {
			returnString += reservationList.getReservation().get(j).getString() + "\n";
		}
		return returnString;
	}

	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}

}
