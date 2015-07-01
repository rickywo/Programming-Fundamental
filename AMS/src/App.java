/* Start up code for SEF Student Management System 2015 semester 1.
 * This code should be used for your initial class diagram.
 * You are free to adapt or completely change the code and design for the
 * extended class diagram and implementation as long as your design allows 
 * for all the requirements and test cases.
 *  
 */

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
	HashMap<String, Course> courses = new HashMap<String, Course>();
	HashMap<String, Venue> venues = new HashMap<String, Venue>();
	HashMap<String, Lecturer> lecturers = new HashMap<String, Lecturer>();
	HashMap<String, Student> students = new HashMap<String, Student>();
	HashMap<String, Tutor> tutors = new HashMap<String, Tutor>();
	HashMap<String, Tutorial> tutorials = new HashMap<String, Tutorial>();
	private int studentNo = 0;
	Scanner scan = new Scanner(System.in);
	private int year = 2015;
	private int semester = 1;
	private String censusDate = "01/05/2015"; 
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	/*
	try {
		 
		Date date = formatter.parse(censusDate);
		System.out.println(date);
		System.out.println(formatter.format(date));
 
	} catch (ParseException e) {
		e.printStackTrace();
	}
	try{
 
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Date date1 = sdf.parse("2009-12-31");
        	Date date2 = sdf.parse("2010-01-31");
 
        	System.out.println(sdf.format(date1));
        	System.out.println(sdf.format(date2));
 
        	if(date1.compareTo(date2)>0){
        		System.out.println("Date1 is after Date2");
        	}else if(date1.compareTo(date2)<0){
        		System.out.println("Date1 is before Date2");
        	}else if(date1.compareTo(date2)==0){
        		System.out.println("Date1 is equal to Date2");
        	}else{
        		System.out.println("How to get here?");
        	}
 
    	}catch(ParseException ex){
    		ex.printStackTrace();
    	}
    }
	*/

	public static void main(String[] args) {
		App a = new App();
		String options[] = { "Add Offering", 
				"Add Lecture", 
				"Assign Lecturer",
				"Add Tutorial", 
				"Assign Tutor", 
				"Admit Student",
				"Enrol Course", 
				"Withdraw Course",
				"Register Tutorial",
				"Deregister Tutorial",
				"Lecturer TimeTable", 
				"Tutor Timetable",
				"Student Timetable",
				"Venue TimeTable", 
				"View Menu" };
		Menu m = new Menu("Main Menu", options);
		int resp;
		do {
			if ((resp = m.getResponse()) == 0)
				break;
			switch (resp) {
			case 1:
				a.handleCreateOffering();
				break;
			case 2:
				a.handleAddLecture();
				break;
			case 3:
				a.handleAssignLecturer();
				break;
			case 4:
				a.handleAddTutorial();
				break;
			case 5:
				a.handleAssignTutor();
				break;
			case 6:
				a.handleAdmitStudent();
				break;
			case 7:
				a.handleEnrolCourse();
				break;
			case 8:
				a.handleWithdrawCourse();
				break;
			case 9:
				a.handleRegisterTutorial();
				break;
			case 10:
				a.handleDeregisterTutorial();
				break;
			case 11:
				a.printLecturerTimetable();
				break;
			case 12:
				a.printTutorTimetable();
				break;
			case 13:
				a.printStudentTimetable();
				break;
			case 14:
				a.printVenueTimetable();
				break;
			case 15:
				String options2[] = { "View Courses", "View Lecturers",
						"View Tutors", "View Venues", "View Students" };
				Menu sub = new Menu("View Submenu", options2);
				int n = 0;
				while ((n = sub.getResponse()) != 0)
					a.view(n);
				break;
			}
		} while (true);
	}

	public App() {
		initializeCourses();
		initializeVenues();
		initializeLecturers();
		initializeTutors();
		initializeStudents();
		//initializeAssignLecturer();

	}

	public void view(int n) {
		if (n == 1)
			displayMap(courses);
		else if (n == 2)
			displayMap(lecturers);
		else if (n == 3)
			displayMap(tutors);
		else if (n == 4)
			displayMap(venues);
		else
			displayMap(students);
	}

	public void displayMap(Map m) {
		Set keySet = m.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			// List elements with no key
			System.out.println(m.get(key));
		}
		hold();
	}

	public void handleCreateOffering() {
		System.out.print("Enter  Course ID : ");
		String courseID = scan.nextLine();
		Course course = getCourse(courseID);

		CourseOffering co;
		if (course == null) {
			System.out.println("Course ID is invalid");
			hold();
		} else {
			System.out.print("Enter Expected Number : ");
			int expectedNum = scan.nextInt();
			scan.nextLine();
			try {
				createOffering(course, expectedNum, year, semester);
			} catch (PreExistException pe) {
				System.out.println(pe);
				hold();
			}
		}
	}

	public CourseOffering createOffering(Course course, int expectedNum,
			int year, int semester) throws PreExistException {
		return course.createOffering(expectedNum, year, semester);
	}

	private void hold() {
		System.out.print("Press enter to continue");
		scan.nextLine();
	}

	public void handleAddLecture() {
		System.out.print("Enter  Course ID : ");
		String courseID = scan.nextLine();
		CourseOffering courseOffering = getCourseOffering(courseID, year,
				semester);
		if (courseOffering == null) {
			System.out.println("No course offering yet");
			hold();
			return;
		}

		System.out.print("Enter  Venue Location : ");
		String location = scan.nextLine();
		Venue venue = getVenue(location);
		if (venue == null) {
			System.out.println("No such venue");
			hold();
			return;
		}

		System.out.print("Enter  Day of Lecture : ");
		int day = scan.nextInt();

		System.out.print("Enter  Start Hour : ");
		double startHour = scan.nextDouble();

		System.out.print("Enter  Duration : ");
		double duration = scan.nextDouble();
		scan.nextLine();
		try {
			courseOffering.assignLecture(day, startHour, duration, venue);
			System.out.println(courseOffering);
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}

	}

	// Function handleAddTutorial()
	public void handleAddTutorial() {
		System.out.print("Enter  Course ID : ");
		String courseID = scan.nextLine();
		CourseOffering courseOffering = getCourseOffering(courseID, year,
				semester);
		if (courseOffering == null) {
			System.out.println("No course offering yet");
			hold();
			return;
		}

		System.out.print("Enter  Venue Location : ");
		String location = scan.nextLine();
		Venue venue = getVenue(location);
		if (venue == null) {
			System.out.println("No such venue");
			hold();
			return;
		}

		System.out.print("Enter  Day of Tutorial : ");
		int day = scan.nextInt();

		System.out.print("Enter  Start Hour : ");
		double startHour = scan.nextDouble();

		System.out.print("Enter  Duration : ");
		double duration = scan.nextDouble();
		scan.nextLine();
		try {
			courseOffering.addTutorial(day, startHour, duration, venue);
			System.out.println(courseOffering);
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}

	}

	public void handleAdmitStudent() {
		System.out.print("Enter  Student Firstname : ");
		String firstname = scan.nextLine();
		System.out.print("Enter  Student Surname : ");
		String surname = scan.nextLine();
		System.out.print("Enter  Student Phone : ");
		String phone = scan.nextLine();
		String key = "" + firstname + ":" + surname + ":" + phone;
		try {
			Student st = admitStudent(firstname, surname, phone);
			System.out.println(st);
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}

	}

	public void assignLecture(CourseOffering co, int day, double startHour,
			double duration, Venue venue) throws ClashException,
			PreExistException {
		co.assignLecture(day, startHour, duration, venue);
	}

	public void handleAssignLecturer() {
		System.out.print("Enter  Course ID : ");
		String courseID = scan.nextLine();
		CourseOffering courseOffering = getCourseOffering(courseID, year,
				semester);
		if (courseOffering == null) {
			System.out.println("No course offering yet");
			hold();
			return;
		}
		Lecture lecture = courseOffering.getLecture();
		if (lecture == null) {
			System.out
					.println("No lecture assigned to this course offering yet ");
			hold();
			return;
		}
		System.out.print("Enter  Lecturer ID : ");
		String lecID = scan.nextLine();
		Lecturer lecturer = getLecturer(lecID);
		if (lecturer == null) {
			System.out.println("No lecturer with such ID ");
			hold();
			return;
		}
		try {
			assignLecturer(lecture, lecturer);
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}
	}

	// Function handleAssignTutor()
	public void handleAssignTutor() {
		System.out.print("Enter  Course ID : ");
		String courseID = scan.nextLine();
		CourseOffering courseOffering = getCourseOffering(courseID, year,
				semester);
		if (courseOffering == null) {
			System.out.println("No course offering yet");
			hold();
			return;
		}
		ArrayList<Tutorial> ts = courseOffering.getTutorials();
		if (ts.size() == 0) {
			System.out
					.println("No tutorial assigned to this course offering yet ");
			hold();
			return;
		}
		int i = 0;
		for (Tutorial temp : ts) {
			System.out.printf("%2d) %s: %s\n", i + 1, temp.getLabel(),
					temp.toString());
			i++;
		}
		System.out.print("Please Chose a Tutorial: ");
		int option = getResponse(ts.size());
		Tutorial t = ts.get(option);

		if (t == null) {
			System.out.println("No Tutorial with such Label ");
			hold();
			return;
		}
		System.out.print("Enter  Tutor ID : ");
		String TutorID = scan.nextLine();
		Tutor tutor = getTutor(TutorID);
		if (tutor == null) {
			System.out.println("No Tutor with such ID ");
			hold();
			return;
		}
		try {
			assignTutor(t, tutor);
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}
	}

	// Function handleEnrolCourse()
	public void handleEnrolCourse() {
		System.out.print("Enter  date of today (dd/mm/yyyy): ");
		String dateString = scan.nextLine();
		
		System.out.print("Enter  Student ID : ");
		String StudentID = scan.nextLine();
		Student student = students.get(StudentID);
		if (student == null) {
			System.out.println("No Student with such ID ");
			hold();
			return;
		}
		System.out.print("Enter  Course ID : ");
		String courseID = scan.nextLine();
		CourseOffering courseOffering = getCourseOffering(courseID, year,
				semester);
		if (courseOffering == null) {
			System.out.println("No course offering yet");
			hold();
			return;
		}
		try {
			enrolCourse(courseOffering, student, dateString);
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}
	}

	// Function handleRegisterTutorial()
	public void handleRegisterTutorial() {
		
		System.out.print("Enter  date of today (dd/mm/yyyy): ");
		String dateString = scan.nextLine();
		System.out.print("Enter  Student ID : ");
		String StudentID = scan.nextLine();
		Student student = students.get(StudentID);
		if (student == null) {
			System.out.println("No Student with such ID ");
			hold();
			return;
		}
		System.out.print("Enter  Course ID : ");
		String courseID = scan.nextLine();
		CourseOffering courseOffering = getCourseOffering(courseID, year,
				semester);
		if (courseOffering == null) {
			System.out.println("No course offering yet");
			hold();
			return;
		}
		ArrayList<Tutorial> ts = courseOffering.getTutorials();
		if (ts.size() == 0) {
			System.out
					.println("No tutorial assigned to this course offering yet ");
			hold();
			return;
		}
		int i = 0;
		for (Tutorial temp : ts) {
			System.out.printf("%2d) %s: %s\n", i + 1, temp.getLabel(),
					temp.toString());
			i++;
		}
		System.out.print("Please Chose a Tutorial: ");
		int option = getResponse(ts.size());
		Tutorial t = ts.get(option);

		if (t == null) {
			System.out.println("No Tutorial with such Label ");
			hold();
			return;
		}
		try {
			registerTutorial(t, student, dateString);
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}
	}
	
	// Function handleRegisterTutorial()
		public void handleDeregisterTutorial() {
			
			System.out.print("Enter  date of today (dd/mm/yyyy): ");
			String dateString = scan.nextLine();
			System.out.print("Enter  Student ID : ");
			String StudentID = scan.nextLine();
			Student student = students.get(StudentID);
			if (student == null) {
				System.out.println("No Student with such ID ");
				hold();
				return;
			}
			System.out.print("Enter  Course ID : ");
			String courseID = scan.nextLine();
			CourseOffering courseOffering = getCourseOffering(courseID, year,
					semester);
			if (courseOffering == null) {
				System.out.println("No course offering yet");
				hold();
				return;
			}
			ArrayList<Tutorial> ts = courseOffering.getTutorials();
			if (ts.size() == 0) {
				System.out
						.println("No tutorial assigned to this course offering yet ");
				hold();
				return;
			}
			int i = 0;
			ArrayList<Tutorial> temp_al = new ArrayList<Tutorial>();
			for (Tutorial temp : ts) {
				ArrayList<Student> sts = temp.getStudents();
				if(sts.contains(student)) {
					System.out.printf("%2d) %s: %s\n", i + 1, temp.getLabel(),
						temp.toString());
					temp_al.add(temp);
					i++;
				}
			}
			System.out.print("Please Chose a Tutorial: ");
			int option = getResponse(ts.size());
			Tutorial t = temp_al.get(option);

			if (t == null) {
				System.out.println("No Tutorial with such Label ");
				hold();
				return;
			}
			try {
				deregisterTutorial(t, student, dateString);
			} catch (ClashException ce) {
				System.out.println(ce);
			} catch (PreExistException pe) {
				System.out.println(pe);
			}
		}
	// Function handleEnrolCourse()
	public void handleWithdrawCourse() {
		System.out.print("Enter  date of today (dd/mm/yyyy): ");
		String dateString = scan.nextLine();
		
		System.out.print("Enter  Student ID : ");
		String StudentID = scan.nextLine();
		Student student = students.get(StudentID);
		if (student == null) {
			System.out.println("No Student with such ID ");
			hold();
			return;
		}
		
		System.out.print("Enter  Course ID : ");
		String courseID = scan.nextLine();
		CourseOffering courseOffering = getCourseOffering(courseID, year,
				semester);
		if (courseOffering == null) {
			System.out.println("No course offering yet");
			hold();
			return;
		}
		try {
			withdrawCourse(courseOffering, student, dateString);
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}
	}

	public void assignLecturer(Lecture lecture, Lecturer lecturer)
			throws ClashException, PreExistException {
		lecturer.assign(lecture);
	}

	public void assignTutor(Tutorial tute, Tutor tutor) throws ClashException,
			PreExistException {
		tutor.assign(tute);
	}

	public Student admitStudent(String firstname, String surname, String phone)
			throws ClashException, PreExistException {
		if (getStudent(firstname, surname, phone) != null)
			throw new PreExistException("Student Already Exists");
		studentNo++;
		String key = "" + firstname + ":" + surname + ":" + phone;
		String sNo = "s4000" + studentNo;
		Student st = new Student(sNo, firstname, surname, phone);
		students.put(sNo, st);
		return st;
	}

	public void enrolCourse(CourseOffering cf, Student st, String dateString)
			throws ClashException, PreExistException {
		try {
			Date tdate = formatter.parse(dateString);
			Date cdate = formatter.parse(censusDate);
			
			if (tdate.compareTo(cdate) > 0) {
				throw new PreExistException("Enrollment needs to be completed before census date");
			}
		}
		catch(ParseException ex){
	    		ex.printStackTrace();
	    }
		st.enrol(cf);
	}

	public void withdrawCourse(CourseOffering cf, Student st, String dateString)
			throws ClashException, PreExistException {
		try {
			Date tdate = formatter.parse(dateString);
			Date cdate = formatter.parse(censusDate);
			
			if (tdate.compareTo(cdate) > 0) {
				throw new PreExistException("Withdraw needs to be completed before census date");
			}
				st.withdraw(cf);
			
		}
		catch(ParseException ex){
	    		ex.printStackTrace();
	    }
		
	}
	
	public void registerTutorial(Tutorial t, Student st, String dateString) 
			throws ClashException, PreExistException {
		try {
			Date tdate = formatter.parse(dateString);
			Date cdate = formatter.parse(censusDate);
			
			if (tdate.compareTo(cdate) > 0) {
				throw new PreExistException("Register needs to be completed before census date");
			}
		}
		catch(ParseException ex){
	    		ex.printStackTrace();
	    }
		st.register(t);
	}
	
	public void deregisterTutorial(Tutorial t, Student st, String dateString) 
			throws ClashException, PreExistException {
		try {
			Date tdate = formatter.parse(dateString);
			Date cdate = formatter.parse(censusDate);
			
			if (tdate.compareTo(cdate) > 0) {
				throw new PreExistException("Deregister needs to be completed before census date");
			}
		}
		catch(ParseException ex){
	    		ex.printStackTrace();
	    }
		st.deregister(t);
	}
	
	public void printLecturerTimetable() {
		System.out.print("Enter  Lecturer ID : ");
		String lecturerID = scan.nextLine();
		Lecturer lecturer = getLecturer(lecturerID);
		if (lecturer == null) {
			System.out.println("No lecturer with this ID");
			return;
		}
		ArrayList<Lecture> lectures = getLectures(lecturer);
		if (lectures == null)
			return;
		for (int i = 0; i < lectures.size(); i++)
			System.out.println(lectures.get(i));
	}

	public void printTutorTimetable() {
		System.out.print("Enter Tutor ID : ");
		String TutorID = scan.nextLine();
		Tutor tutor = getTutor(TutorID);
		if (tutor == null) {
			System.out.println("No Tutor with this ID");
			return;
		}
		ArrayList<Tutorial> tutes = getTutorials(tutor);
		if (tutes == null)
			return;
		for (Tutorial tute : tutes)
			System.out.println(tute);
	}

	public void printVenueTimetable() {
		System.out.print("Enter  Venue Location : ");
		String location = scan.nextLine();
		Venue venue = getVenue(location);
		if (venue == null) {
			System.out.println("No Venue at this location");
			return;
		}
		ArrayList<Lesson> lessons = getLessons(venue);
		if (lessons == null)
			return;
		for (int i = 0; i < lessons.size(); i++)
			System.out.println(lessons.get(i));
	}
	
	public void printStudentTimetable() {
		System.out.print("Enter  Student ID : ");
		String studentID = scan.nextLine();
		Student student = students.get(studentID);
		if (student == null) {
			System.out.println("No Student with this ID");
			return;
		}
		ArrayList<CourseOffering> enrolledcourses = student.getCourseOfferings();
		if (enrolledcourses == null)
			return;
		for (CourseOffering cf: enrolledcourses) {
			System.out.println(cf.getLecture());
			ArrayList<Tutorial> ts = cf.getTutorials();
			if(ts.size()!=0) {
				for(Tutorial t: ts) {
					ArrayList<Student> sts = t.getStudents();
					if(sts.contains(student))
						System.out.println(t);
				}
			}
		}
	}

	public void initializeCourses() {
		Course course1 = new Course("P101", "Programming 1",
				"Teach Basic Programming");
		Course course2 = new Course("P102", "Programming 2",
				"Teach Intermediate Programming");
		Course course3 = new Course("S101", "Software Engineering",
				"Teach UML and Modelling");
		Course course4 = new Course("WP1", "Web Programming",
				"Teach Web Technologies");
		Course course5 = new Course("UI1", "User Interface",
				"Teach UI Principles");
		Course course6 = new Course("Math", "Discret Maths",
				"Teach Maths needed for CS");
		Course course7 = new Course("Net1", "Networkins",
				"Teach networking principles");

		course3.addPrereq(course1);
		course2.addPrereq(course1);
		course7.addPrereq(course2);
		course7.addPrereq(course6);

		courses.put("P101", course1);
		courses.put("P102", course2);
		courses.put("S101", course3);
		courses.put("WP1", course4);
		courses.put("UI1", course5);
		courses.put("Math", course6);
		courses.put("Net1", course7);

	}

	public void initializeVenues() {
		venues.put("12.10.02", new Venue("12.10.02", 120, "Lecture"));
		venues.put("12.10.03", new Venue("12.10.03", 200, "Lecture"));
		venues.put("10.10.22", new Venue("10.10.22", 36, "TuteLab"));
		venues.put("10.10.23", new Venue("10.10.23", 36, "TuteLab"));
	}

	public void initializeLecturers() {
		lecturers.put("e44556", new Lecturer("e44556", "Tim O'Connor",
				"Lecturer", "14.13.12"));
		lecturers.put("e44321", new Lecturer("e44321", "Richard Cooper",
				"Professor", "14.13.12"));
		lecturers.put("e54321", new Lecturer("e54321", "Jane Smith",
				"Lecturer", "11.9.10"));
	}

	public void initializeTutors() {
		tutors.put("e45666", new Tutor("e45666", "Sam Murphy"));
		tutors.put("e45777", new Tutor("e45777", "Geoff Ware"));
		tutors.put("e45888", new Tutor("e45888", "Jeorge Klein"));
	}

	public void initializeAssignLecturer() {
		try {
			CourseOffering offering1 = createOffering(courses.get("P101"), 200,
					2015, 1);
			CourseOffering offering2 = createOffering(courses.get("P102"), 200,
					2015, 1);
			assignLecture(offering1, 2, 10.0, 2.0, venues.get("12.10.02"));
			assignLecture(offering2, 3, 14.5, 2.0, venues.get("12.10.03"));
			assignLecturer(offering1.getLecture(), lecturers.get("e44556"));
			assignLecturer(offering2.getLecture(), lecturers.get("e44321"));
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}
	}

	public void initializeStudents() {
		Student student1 = null;
		Student student2 = null;
		Student student3 = null;
		try {
			student1 = admitStudent("Micheal", "Jordan", "0412345678");
			student2 = admitStudent("Brad", "Pitt", "0412345876");
			student3 = admitStudent("Ben", "Stiller", "0412345888");
		} catch (ClashException ce) {
			System.out.println(ce);
		} catch (PreExistException pe) {
			System.out.println(pe);
		}

		// Student 1 has 2 prerequisites
		student1.addPrereq(getCourse("P102"));
		student1.addPrereq(getCourse("Math"));
		// Student 2 has 1 prerequisites
		student2.addPrereq(getCourse("P101"));
	}

	public ArrayList<Lecture> getLectures(Lecturer lecturer) {
		return lecturer.getLectures();
	}

	public ArrayList<Tutorial> getTutorials(Tutor tutor) {
		return tutor.getTutorials();
	}

	public ArrayList<Lesson> getLessons(Venue venue) {
		return venue.getLessons();
	}

	public Lecturer getLecturer(String eNo) {
		return lecturers.get(eNo);
	}

	public Tutor getTutor(String eNo) {
		return tutors.get(eNo);
	}

	public Venue getVenue(String location) {
		return venues.get(location);
	}

	public Course getCourse(String ID) {
		return courses.get(ID);
	}

	public Student getStudent(String firstname, String surname, String phone) {
		Student student = null;
		for (Student st : students.values()) {
			if (firstname.equals(st.getFirstname())
					&& surname.equals(st.getSurname())
					&& phone.equals(st.getPhone())) {
				student = st;
			}
		}
		return student;
	}

	public Lecture getLecture(CourseOffering offering) {
		return offering.getLecture();
	}

	public CourseOffering getCourseOffering(String ID, int year, int sem) {
		Course c = courses.get(ID);
		if (c == null)
			return null;
		return c.getOffering(year, semester);
	}

	int getResponse(int op) {
		int num = -1;
		do {
			String s;
			try {
				s = scan.nextLine();
				num = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				continue;
			}
		} while (num < 1 || num >= op + 1);
		return num - 1;
	}
}