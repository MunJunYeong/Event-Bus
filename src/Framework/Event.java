/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Framework;

import java.io.Serializable;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L; //Default serializable value  
	private EventId eventId;
	private String message;

	public Event(EventId id, String text) {
		this.message = text;
		this.eventId = id;
	}
	public EventId getEventId() {
		return eventId;
	}
	public String getMessage() {
		return message;
	}
}