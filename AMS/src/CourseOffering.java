import java.util.ArrayList;

public class CourseOffering {
	private Lecture lecture;
	private ArrayList<Tutorial> tutorials;
	private int expectedStNo;
	private Course course;
	private int year;
	private int semester;

	public CourseOffering(int expectedStNo, int year, int sem) {
		this.expectedStNo = expectedStNo;
		String key = "" + year + ":" + sem;
		this.year = year;
		this.semester = sem;
		lecture = null;
		tutorials = new ArrayList<Tutorial>();
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	public Course getCourse() {
		return course;
	}

	public void assignLecture(int lectureDay, double lectureStartHr,
			double lectureDur, Venue venue) throws ClashException,
			PreExistException {
		if (lecture != null)
			throw new PreExistException("Lecuture already exist");
		lecture = new Lecture(lectureDay, lectureStartHr, lectureDur, venue);
	}

	// Administrator can add tutorials specifying date, time and venue.
	// Tutorials for each course
	// offering should be labeled T1, T2, ...
	public void addTutorial(int tuteDay, double tuteStartHr, double tuteDur,
			Venue venue) throws ClashException, PreExistException {
		int i = tutorials.size();
		String key = "T" + Integer.toString(i + 1);
		Tutorial tutorial = new Tutorial(tuteDay, tuteStartHr, tuteDur, venue,
				key);
		tutorials.add(tutorial);
	}

	public Lecture getLecture() {
		return lecture;
	}
	
	public ArrayList<Tutorial> getTutorials() {
		return tutorials;
	}

	public String toString() {
		String s = "";
		if (course != null) {
			s = "Id = " + course.getId();
			s += "\nName = " + course.getName();
		}
		s += "\nYear = : " + year + " Semester : " + semester;
		s += "\nExpected student number " + expectedStNo;
		if (lecture != null)
			s += "\nLecture: " + lecture.toString();
		s += "\nTutorials: ";
		for(Tutorial t: tutorials) {
			s += "\n -" + t.getLabel() +" : "+ t.toString();
		}
		return s;
	}
}