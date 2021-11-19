package Components.Reservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReservationComponent {
	protected ArrayList<Reservation> vReservation;
	

	public ReservationComponent(String fileName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		this.vReservation = new ArrayList<Reservation>();
		while(bufferedReader.ready()) {
			String reservationInfo = bufferedReader.readLine();
			if(!reservationInfo.equals("")) {
				this.vReservation.add(new Reservation(reservationInfo));
			}
		}
		bufferedReader.close();
	}
	public boolean addReservation(String reservationData) {
		if(this.vReservation.add(new Reservation(reservationData))) {
			addReservationData();
			return true;
		}else {
//			this.errorMessage = "������û�� �����߽��ϴ�.";
			return false;
		}
	}
	public void addReservationData() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("../bin/Reservation.txt"));
			for(int i =0; i<this.vReservation.size(); i++) {
				String reservation = this.vReservation.get(i).toString();
				bw.write(reservation);
				bw.write("\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<Reservation> getAllReservationData()   {
		return this.vReservation;
	}
	public String getReservationCourse(String studentId) {
		String reservationCourse = null;
		if(this.vReservation.size()==0) {
//			this.errorMessage = "������û ������ �����ϴ�.";
			return null;
		}
		for(int i =0; i<this.vReservation.size(); i++) {
			Reservation reservation = (Reservation)this.vReservation.get(i);
			if(reservation.match(studentId)) {
				reservationCourse = reservation.getReservationCourse();
			}else {
//				this.errorMessage = "�л� ID�� ��ġ���� �ʽ��ϴ�.";
			}
		}
		return reservationCourse;
	}
	public boolean deleteReservation(String studentId, String courseId) {
		for(int i=0; i <= this.vReservation.size(); i++) {
			Reservation reservation = this.vReservation.get(i);
			if(reservation.match(studentId, courseId)) {
				if(this.vReservation.remove(reservation)) {
					addReservationData();
					return true;
				}
			}
		}
//		this.errorMessage = "������û ������ �����ϴ�.";
		return false;
	}

	public ArrayList<Reservation> getReservation() {
		return vReservation;
	}
	
}
