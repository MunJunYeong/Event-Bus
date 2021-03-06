package Components.Reservation;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.StringTokenizer;

import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class ReservationMain {
	public static void main(String[] args) throws NotBoundException, IOException, InterruptedException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** ReservationMain(ID:" + componentId + ") is successfully registered. \n");

		ReservationComponent reservationList = new ReservationComponent("Reservations.txt");
		
		Event event = null;
		boolean done = false;
		while(!done) {
			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent();
				switch (event.getEventId()) {
				case ListReservations:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeReservationList(reservationList)));
					break;
				case RegisterReservation:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, postReservation(event.getMessage(), reservationList)));
					break;
				case DeleteReservation :
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, deleteReservation(event.getMessage(), reservationList)));
					break;
				case QuitTheSystem :
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
					}
				}
			}
		}

	private static String deleteReservation(String message, ReservationComponent reservationList) {
		StringTokenizer stringTokenizer = new StringTokenizer(message);
		String studentId = stringTokenizer.nextToken();
		String courseId = stringTokenizer.nextToken();
		for(int i=0; i<reservationList.vReservation.size(); i++) {
			if(reservationList.vReservation.get(i).studentId.equals(studentId)&& reservationList.vReservation.get(i).courseId.equals(courseId) ) {
				reservationList.vReservation.remove(i);
				return "This reservation is successfully deleted";
			}
		}
		return "Student ID & Course ID is not exist";
	}

	private static String postReservation(String message, ReservationComponent reservationList) {
		if(message.equals("1")) {
			return "not exist studentId";
		}
		if(message.equals("2")) {
			return "not exist courseId";
		}
		if(message.equals("3")) {
			return "not fulfill reservation";
		}
		StringTokenizer stringTokenizer = new StringTokenizer(message);
		String studentId = stringTokenizer.nextToken();
		String courseId = stringTokenizer.nextToken();
		for(int i=0; i<reservationList.vReservation.size(); i++) {
			if(reservationList.vReservation.get(i).studentId.equals(studentId)&& reservationList.vReservation.get(i).courseId.equals(courseId) ) {
				return "duplicated reservation data";
			}
		}
		Reservation res = new Reservation(message);
		reservationList.vReservation.add(res);
		return "success registry reservation";
	}

	private static String makeReservationList(ReservationComponent reservationList) {
		String returnString = "";
		if(reservationList.vReservation.size() ==0) {
			return "No ReservationList";
		}
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
