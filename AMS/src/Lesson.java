import java.util.ArrayList;

public abstract class Lesson {
	private double startHour;
	private double endHour;
	private int day;
	private Staff staff;
	private ArrayList<Student> students;
	private Venue venue;

	public Lesson(int lecDay, double lecstartHour, double lecDur, Venue venue)
			throws ClashException {
		this.day = lecDay;
		this.startHour = lecstartHour;
		this.endHour = lecstartHour + lecDur;
		venue.checkClash(day, startHour, endHour);
		this.venue = venue;
		venue.addLesson(this);
		staff = null;
		students = new ArrayList<Student>();
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Staff getStaff() {
		return staff;
	}

	public void addStudent(Student student) {
		this.students.add(student);
	}

	public void removeStudent(Student student) {
		this.students.remove(student);
	}

	public ArrayList<Student> getStudents() {
		return students;
	}

	public double getStart() {
		return startHour;
	}

	public double getEnd() {
		return endHour;
	}

	public Venue getVenue() {
		return venue;
	}

	public int getDay() {
		return day;
	}

	public boolean isFull() {
		if (students.size() < venue.getCapacity()) {
			return false;
		}
		return true;
	}

	public String toString() {
		String s = "Venue = " + venue.getLocation();
		s += " Day = " + day;
		s += " Start time = " + startHour;
		s += " End time = " + endHour;
		if (staff != null)
			s += " Staff " + staff.getName();
		return s;
	}
}