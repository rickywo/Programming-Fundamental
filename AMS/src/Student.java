import java.util.ArrayList;

public class Student {
	private final int MAX_ENROLLED_COURSES = 4;
	private String sNo;
	private String firstname;
	private String surname;
	private String phone;
	private ArrayList<Course> prerequisites;
	private ArrayList<CourseOffering> enrolledcourses;

	public Student(String sNo, String firstname, String surname, String phone) {
		this.sNo = sNo;
		this.firstname = firstname;
		this.surname = surname;
		this.phone = phone;
		prerequisites = new ArrayList<Course>();
		enrolledcourses = new ArrayList<CourseOffering>();
	}

	public void enrol(CourseOffering courseoffering) throws ClashException,
			PreExistException {
		// Check if not satisfying prerequisites requirement
		Course c = courseoffering.getCourse();
		ArrayList<Course> ac = c.getPrereqs();
		int i = 0, match = 0;
		while (i < ac.size()) {
			Course next = ac.get(i);
			if (prerequisites.contains(next)) {
				match++;
				System.out.println(i);
			}
			i++;
		}
		if (match != i)
			throw new PreExistException(
					"Not met the requirement of prerequisites");

		// Check if exceed the max number of enrolled courses(4)
		if (exceedMaxenrol())
			throw new PreExistException(
					"Exceed the maxmum number(4) of enrolled courses");
		Lecture courselecture = courseoffering.getLecture();
		if(courselecture == null)
			throw new PreExistException("Lecture is not assigned");
		// Check if the lecture of this course is currently full
		if (courselecture.isFull())
			throw new PreExistException("Course is already full");
		// Check if the lecture of this course is clash to other courses
		i = 0;

		while (i < enrolledcourses.size()) {
			CourseOffering nextcourse = enrolledcourses.get(i);
			Lecture next = nextcourse.getLecture();
			if (next.getDay() == courselecture.getDay()) {
				if (overlap(courselecture.getStart(), courselecture.getEnd(),
						next.getStart(), next.getEnd()))
					throw new ClashException("Lecture Clash");
				;
			}
			i++;
		}

		// Add this course into enrolledcourses arraylist
		if(enrolledcourses.contains(courseoffering))
			throw new PreExistException("Has been enrolled in this course");
		enrolledcourses.add(courseoffering);
		// Add this student into the lecture of this course
		courselecture.addStudent(this);
	}

	public void withdraw(CourseOffering courseoffering) throws ClashException,
			PreExistException {
		// Check if not being enrolled in this course
		if (!(enrolledcourses.contains(courseoffering))) {
			throw new PreExistException("Not being enrolled in this course");
		}
		// Check if still registered in any tute of this course
		ArrayList<Tutorial> tutes = courseoffering.getTutorials();

		int i = 0;
		while (i < tutes.size()) {
			Tutorial next = tutes.get(i);
			ArrayList<Student> sts = next.getStudents();
			if (sts.contains(this)) {
				throw new PreExistException(
						"Not being deregistered from the tutorial of this course");
			}
			i++;
		}

		Lecture courselecture = courseoffering.getLecture();
		// Remove this course into enrolledcourses arraylist
		enrolledcourses.remove(courseoffering);
		// Remove this student into the lecture of this course
		courselecture.removeStudent(this);
	}

	public void register(Tutorial tute) throws ClashException,
			PreExistException {
		boolean isExist = false;
		int i = 0;
		// Check if this student is registered in this tute
		if ((tute.getStudents()).contains(this)) {
			throw new PreExistException(
					"Student is being registered in this tutorial");
		}
		// Check if this student is being registered in other tutorial of this
		// course
		while (i < enrolledcourses.size()) {
			CourseOffering courseoffering = enrolledcourses.get(i);
			ArrayList<Tutorial> tutes_t = courseoffering.getTutorials();
			if (tutes_t.contains(tute)) {
				for (Tutorial temp : tutes_t) {
					if ((temp.getStudents()).contains(this)) {
						throw new ClashException(
								"Student is being registerd in other tutorial of same course");
					}
				}
				isExist = true;
				break;
			}
			i++;
		}
		// Check if this student is being enrolled in the course of this tute
		if (!isExist) {
			throw new PreExistException(
					"Not being enrolled in the course of this tutorial");
		}
		// Check if this tute is full
		if (tute.isFull()) {
			throw new PreExistException("This tutorial is full");
		}

		tute.addStudent(this);
	}

	public void deregister(Tutorial tute) throws ClashException,
			PreExistException {
		// Check if this student is not registered in this tute
		if (!(tute.getStudents()).contains(this)) {
			throw new PreExistException(
					"Student is not being registerd in this tutorial");
		}
		tute.removeStudent(this);
	}

	public void addPrereq(Course preq) {
		prerequisites.add(preq);
	}

	public String toString() {
		return "sNo: " + sNo + " firstname: " + firstname + " surname: "
				+ surname + " phone: " + phone;
	}

	public String getSNo() {
		return sNo;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getSurname() {
		return surname;
	}

	public String getPhone() {
		return phone;
	}

	public ArrayList<Course> getPrereqs() {
		return prerequisites;
	}
	
	public ArrayList<CourseOffering> getCourseOfferings() {
		return enrolledcourses;
	}

	private boolean exceedMaxenrol() {
		if (enrolledcourses.size() >= MAX_ENROLLED_COURSES) {
			return true;
		}
		return false;
	}

	private boolean overlap(double start1, double end1, double start2,
			double end2) {
		if (inBetween(start1, start2, end2) || inBetween(end1, start2, end2))
			return true;
		else
			return false;
	}

	private boolean inBetween(double x, double start, double end) {
		if (x > start && x < end)
			return true;
		else
			return false;
	}
}
