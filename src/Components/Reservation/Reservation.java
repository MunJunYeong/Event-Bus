package Components.Reservation;

import java.util.StringTokenizer;

public class Reservation {
	
	protected String studentId;
	protected String courseId;
	
	public Reservation(String inputString) {
		StringTokenizer stringTokenizer = new StringTokenizer(inputString);
		
		this.studentId = stringTokenizer.nextToken();
		this.courseId = stringTokenizer.nextToken();
		
	}
	
	
	
	public String toString() {
        String stringReturn = this.studentId + " " + this.courseId;
        return stringReturn;
    }

	public boolean match(String studentId, String courseId) {
		if(this.studentId.equals(studentId) && this.courseId.equals(courseId)) return true;
	      else return false;
	}

	public String getReservationCourse() {
		return this.courseId;
	}



	public boolean match(String studentId) {
		return this.studentId.equals(studentId);
	}
	public String getString() {
		String stringReturn = this.studentId + " " + this.courseId;
		return stringReturn;
	}








}
