/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Framework;

import java.io.Serializable;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L; //Default serializable value  
	private EventId eventId;
	private String message;
	private String api;

	public Event(EventId id, String text, String api) {
		this.message = text;
		this.eventId = id;
		this.api = api;
	}
	public Event(EventId clientoutput, String makeStudentList) {
		this.message = makeStudentList;
		this.eventId = clientoutput;
		this.api = "output";
	}
	public EventId getEventId() {
		return eventId;
	}
	public String getMessage() {
		return message;
	}
	public String getApi() {
		return api;
	}
}