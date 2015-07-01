import org.junit.*;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class AppTest {
	App app;
	Course course1;
	Course course2;
	Course course3;
	Course course4;
	Course course5;
	Student student1;
	Student student2;
	Tutorial tutorial1;
	Tutorial tutorial2;
	Tutor tutor1;
	//surname surname1;
	@Before
	public void setUp() {
		app = new App();
		course1 = app.courses.get("P101");
		course2 = app.courses.get("P102");
		course3 = app.courses.get("UI1");
		course4 = app.courses.get("WP1");
		course5 = app.courses.get("P103");
		student1 = app.students.get("s40001");
		student2 = app.students.get("s40002");
		tutorial1 = app.tutorials.get("cs1061");
	}

	@After
	public void tearDown() {

	}

	// Checking there can be no two offerings for the same course in the same
	// year and semester
	@Test(expected = PreExistException.class)
	public void testNoDuplicateOfferings() throws PreExistException,
			ClashException {
		assertEquals("P101", course1.getId());
		app.createOffering(course1, 200, 2015, 1);
		app.createOffering(course1, 200, 2015, 1);
	}

	// Checking a student (identified uniquely by first-name, surname and phone)
	// to be admitted twice : Chang-Yi Wu
	@Test(expected = PreExistException.class)
	public void testNoDuplicateAdmitting() throws PreExistException,
			ClashException {
		app.admitStudent("Kobe", "Bryant", "0412345699");
		app.admitStudent("Kobe", "Bryant", "0412345699");
	}

	// Checking offerings created can be accessed via getCourseOffering
	@Test
	public void testGetCourseOffering() throws PreExistException {
		app.createOffering(course1, 200, 2015, 1);
		assertNotEquals("Offering for 2015 semester 1 exists",
				app.getCourseOffering("P101", 2015, 1), null);
		assertEquals("Offering for 2016 does not exist",
				app.getCourseOffering("P101", 2016, 1), null);
	}

	// Checking venue clashes are detected
	@Test(expected = ClashException.class)
	public void testVenueClashes() throws PreExistException, ClashException {
		CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
		CourseOffering offering2 = app.createOffering(course2, 200, 2015, 1);
		Venue venue = app.getVenue("12.10.02");
		app.assignLecture(offering1, 3, 10.0, 2.0, venue);
		app.assignLecture(offering2, 3, 11.5, 2.0, venue);
	}

	// Checking prevention of student enrolling in a offering course without met
	// required prerequisites: Chang-Yi Wu
	@Test(expected = PreExistException.class)
	public void testStudentEnrollment() throws PreExistException,
			ClashException {
		CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
		app.enrolCourse(offering1, student1, "01/05/2015");
	}
	
	// Checking prevention of student enrolling after census day: Chang-Yi Wu
		@Test(expected = PreExistException.class)
		public void testEnrolExceedCensusDay() throws PreExistException,
				ClashException {
			ArrayList<CourseOffering> cfs = null;
			CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
			Venue venue1 = app.getVenue("12.10.02");
			app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
			app.enrolCourse(offering1, student1, "01/05/2015");
			cfs = student1.getCourseOfferings();
			assertTrue(cfs.contains(offering1));
			CourseOffering offering2 = app.createOffering(course2, 200, 2015, 1);		
			Venue venue2 = app.getVenue("12.10.03");
			app.assignLecture(offering2, 3, 11.5, 2.0, venue2);
			app.enrolCourse(offering2, student1, "02/05/2015");
		}

	// Checking lecture clashes are detected
	@Test(expected = ClashException.class)
	public void testLectureClashes() throws PreExistException, ClashException {

		CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
		CourseOffering offering2 = app.createOffering(course2, 200, 2015, 1);
		Venue venue1 = app.getVenue("12.10.02");
		Venue venue2 = app.getVenue("12.10.03");
		app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
		app.assignLecture(offering2, 3, 11.5, 2.0, venue2);
		Lecturer lecturer = app.getLecturer("e44556");
		Lecture lecture1 = offering1.getLecture();
		Lecture lecture2 = offering2.getLecture();
		app.assignLecturer(lecture1, lecturer);
		app.assignLecturer(lecture2, lecturer);
	}

	// Checking tutorial time clashes of a tutor is detected: Chang-Yi Wu
	@Test(expected = ClashException.class)
	public void testTutorialClashes() throws PreExistException, ClashException {

		CourseOffering offering = app.createOffering(course1, 200, 2015, 1);
		Venue venue1 = app.getVenue("12.10.02");
		Venue venue2 = app.getVenue("12.10.03");
		offering.addTutorial(3, 10.0, 2.0, venue1);
		offering.addTutorial(3, 11.5, 2.0, venue2);
		Tutor tutor = new Tutor("e88998", "Tim Ryan");
		ArrayList<Tutorial> ts = offering.getTutorials();
		Tutorial tute1 = ts.get(0);
		Tutorial tute2 = ts.get(1);
		app.assignTutor(tute1, tutor);
		app.assignTutor(tute2, tutor);
	}

	// Checking venue clashes of two tutorial is detected: Chang-Yi Wu
	@Test(expected = ClashException.class)
	public void testTutorialVenueClashes() throws PreExistException, ClashException {

		CourseOffering offering = app.createOffering(course1, 200, 2015, 1);
		Venue venue = app.getVenue("12.10.02");
		offering.addTutorial(3, 10.0, 2.0, venue);
		offering.addTutorial(3, 11.5, 2.0, venue);
	}

	// Checks when adding a lectures Venue assignments are correctly made
	@Test
	public void testVenueAssignments() throws PreExistException, ClashException {
		CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
		CourseOffering offering2 = app.createOffering(course2, 200, 2015, 1);
		CourseOffering offering3 = app.createOffering(course3, 200, 2015, 1);
		CourseOffering offering4 = app.createOffering(course4, 200, 2015, 1);
		Venue venue1 = app.getVenue("12.10.02");
		Venue venue2 = app.getVenue("12.10.03");
		app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
		app.assignLecture(offering2, 3, 14.5, 2.0, venue2);
		app.assignLecture(offering3, 4, 9.5, 2.0, venue2);
		app.assignLecture(offering4, 5, 18.5, 2.0, venue2);
		assertEquals(app.getLessons(venue1).size(), 1);
		assertEquals(app.getLessons(venue2).size(), 3);
	}
	

	//Check registering into a tutorial twice
	@Test(expected = PreExistException.class)
	public void testRegisterTutorial() throws PreExistException, ClashException {
	  
	   assertEquals("P101", course1.getId());
      app.registerTutorial(tutorial1, student1, null);
      app.registerTutorial(tutorial1, student1, null);
	   }
	
	//Checks student ID
	@Test(expected = PreExistException.class)
	public void testEqualStuID() throws PreExistException, ClashException{
	   assertEquals("s40001", app.students.get("s40001"));
	  
	}
	//check any tutor from being assigned two tutorials at the same time
	@Test(expected = PreExistException.class)
   public void testassigntutorial() throws PreExistException, ClashException{
	   app.assignTutor(tutorial1, tutor1);
	   app.assignTutor(tutorial1, tutor1);
	   
	}
	//check overlapping tutorials in any venue
	@Test(expected = PreExistException.class)
   public void testoverlappingtutorial() throws PreExistException, ClashException{
	
	   CourseOffering offering = app.createOffering(course1, 200, 2015, 1);
      Venue venue1 = app.getVenue("12.10.02");
      offering.addTutorial(3, 10.0, 2.0, venue1);
      offering.addTutorial(3, 10.0, 2.0, venue1);
	}
	//check more than one offering per course in any semester
   @Test(expected = PreExistException.class)
   public void testoveroffering() throws PreExistException, ClashException{
      app.createOffering(course1, 200, 2015, 1);
      app.createOffering(course1, 200, 2015, 1);
   }

	//check the system can prevent student enrolling more than 4 courses
   @Test(expected = PreExistException.class)
   public void teststudentenroll4() throws PreExistException, ClashException{
      Course course5 = new Course("e01","","");
      Course course6 = new Course("e02","","");
      Course course7 = new Course("e03","","");
      Course course8 = new Course("e04","","");
      Course course9 = new Course("e05","","");
      
      CourseOffering offering1 = app.createOffering(course6, 200, 2015, 1);
      CourseOffering offering2 = app.createOffering(course7, 200, 2015, 1);
      CourseOffering offering3 = app.createOffering(course8, 200, 2015, 1);
      CourseOffering offering4 = app.createOffering(course9, 200, 2015, 1);
      CourseOffering offering5 = app.createOffering(course5, 200, 2015, 1);
      
      Venue venue1 = app.getVenue("12.10.02");
      Venue venue2 = app.getVenue("12.10.03");
      app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
      app.assignLecture(offering2, 3, 14.5, 2.0, venue2);
      app.assignLecture(offering3, 4, 9.5, 2.0, venue2);
      app.assignLecture(offering4, 5, 18.5, 2.0, venue2);
      app.assignLecture(offering5, 2, 18.5, 2.0, venue2);
      
      student1.enrol(offering1);
      student1.enrol(offering2);
      student1.enrol(offering3);
      student1.enrol(offering4);
      student1.enrol(offering5);
      
   }
   
   //check the system can prevent student enrolling in the same courses twice
   @Test(expected = PreExistException.class)
   public void testenrollcoursetwice() throws PreExistException, ClashException{
      CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
      Venue venue1 = app.getVenue("12.10.02");
      app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
      Lecturer lecturer = app.getLecturer("e44556");
      Lecture lecture1 = offering1.getLecture();
      app.assignLecturer(lecture1, lecturer);
      student1.enrol(offering1);
      student1.enrol(offering1);
   }
   
   //check the system can prevent student register in the same tutorials twice
   @Test(expected = PreExistException.class)
   public void testenrolltutetwice() throws PreExistException, ClashException{
      CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
      Venue venue1 = app.getVenue("12.10.02");
      app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
      Venue venue2 = app.getVenue("12.10.02");
      offering1.addTutorial(3, 10.0, 2.0, venue2);
      ArrayList<Tutorial> ts = offering1.getTutorials();
      Tutorial tute1 = ts.get(0);
      student1.enrol(offering1);
      student1.register(tute1);
      student1.register(tute1);
   }
   
   //check the system can prevent student enroll in the course which is full
   @Test(expected = PreExistException.class)
   public void testcourseisfull() throws PreExistException, ClashException{
      CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
      Venue venue1 = new Venue("12.9.10",1,"offering1");
      app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
      student1.enrol(offering1);
      student2.enrol(offering1);
      
   }
   
   //check the system can prevent student enroll in the tutorial which is full
   @Test(expected = PreExistException.class)
   public void testtuteisfull() throws PreExistException, ClashException{
      CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
      Venue venue1 = app.getVenue("12.10.02");
      app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
      Venue venue2 = new Venue("12.9.10",1,"offering1");
      offering1.addTutorial(3, 10.0, 2.0, venue2);
      ArrayList<Tutorial> ts = offering1.getTutorials();
      Tutorial tute1 = ts.get(0);
      student1.enrol(offering1);
      student2.enrol(offering1);
      student1.register(tute1);
      student2.register(tute1);
   }
   
   //check the system can prevent student deregister the lecture before deregister the tutorial
   @Test(expected = PreExistException.class)
   public void testderegister() throws PreExistException, ClashException{
      CourseOffering offering1 = app.createOffering(course1, 200, 2015, 1);
      Venue venue1 = app.getVenue("12.10.02");
      app.assignLecture(offering1, 3, 10.0, 2.0, venue1);
      Venue venue2 = new Venue("12.9.10",24,"offering1");
      offering1.addTutorial(4, 10.0, 2.0, venue2);
      ArrayList<Tutorial> ts = offering1.getTutorials();
      Tutorial tute1 = ts.get(0);
      student1.enrol(offering1);
      student1.register(tute1);
      student1.withdraw(offering1);
      student1.deregister(tute1);
   }
   
   //Test that System should not allow students to register from tutorials after the census date
   @Test(expected = PreExistException.class)
   public void testCensusDateRegistration() throws PreExistException, ClashException {
	   try{
		   CourseOffering offeredCourse = app.createOffering(course2, 100, 2015, 1);
		   Venue courseVenue = app.getVenue("12.10.03");
		   app.assignLecture(offeredCourse, 1, 13.0, 2.0, courseVenue);
		   Venue tutorialVenue = new Venue ("14.09.16", 200, "Tutorial");
		   offeredCourse.addTutorial(2, 17.30, 1.0, tutorialVenue);
		   ArrayList<Tutorial> tutorialList = offeredCourse.getTutorials();
		   Tutorial courseTutorial = tutorialList.get(0);
		   app.registerTutorial(courseTutorial, student1, "02/05/2015");
	   } catch (PreExistException ex) {
		   System.out.println(ex);
	   }
   }
   
 //Test that System should not allow students to deregister from tutorials after the census date
   @Test(expected = PreExistException.class)
   public void testCensusDateDeregister() throws PreExistException, ClashException {
	   try {
		   CourseOffering offeredCourse = app.createOffering(course2, 100, 2015, 1);
		   Venue courseVenue = app.getVenue("12.10.03");
		   app.assignLecture(offeredCourse, 1, 13.0, 2.0, courseVenue);
		   Venue tutorialVenue = new Venue ("14.09.16", 200, "Tutorial");
		   offeredCourse.addTutorial(2, 17.30, 1.0, tutorialVenue);
		   ArrayList<Tutorial> tutorialList = offeredCourse.getTutorials();
		   Tutorial courseTutorial = tutorialList.get(0);
		   app.deregisterTutorial(courseTutorial, student1, "02/05/2015");
	   } catch (PreExistException ex) {
		   System.out.println(ex);
	   }
   }
   
  //Test that System should prevent students from registering into a tutorial twice
   @Test(expected = ClashException.class)
   public void testRegisterTutorialTwice() throws PreExistException, ClashException {
	   try {
	   CourseOffering offeredCourse = app.createOffering(course1, 100, 2015, 1);
	   Venue courseVenue = app.getVenue("12.10.03");
	   app.assignLecture(offeredCourse, 1, 13.0, 2.0, courseVenue);
	   Venue tutorialVenueOne = new Venue ("14.09.16", 200, "Tutorial");
	   Venue tutorialVenueTwo = new Venue ("13.10.12", 200, "Tutorial");
	   offeredCourse.addTutorial(2, 17.30, 1.0, tutorialVenueOne);
	   offeredCourse.addTutorial(3, 10.30, 1.0, tutorialVenueTwo);
	   ArrayList<Tutorial> tutorialList = offeredCourse.getTutorials();
	   Tutorial courseTutorialOne = tutorialList.get(0);
	   Tutorial courseTutorialTwo = tutorialList.get(1);
	   app.enrolCourse(offeredCourse, student1, "01/04/2015");
	   app.registerTutorial(courseTutorialOne, student1, "01/05/2015");
	   app.registerTutorial(courseTutorialTwo, student1, "01/05/2015");
	   } catch(ClashException ex) {
		   System.out.println(ex);
	   }
   }
   
   //Test that System should prevent two Lecturers being assigned to one Lecture
   @Test(expected = PreExistException.class)
   public void testPreventAssigningTwoLecturers() throws PreExistException, ClashException {
	   try {
	   CourseOffering offeredCourse = app.createOffering(course1, 100, 2015, 1);
	   Venue courseVenue = app.getVenue("12.10.03");
	   app.assignLecture(offeredCourse, 1, 13.0, 2.0, courseVenue);
	   Lecturer lecturerOne = app.getLecturer("e54321");
	   Lecturer lecturerTwo = app.getLecturer("e44321");
	   Lecture courseLecture = offeredCourse.getLecture();
	   app.assignLecturer(courseLecture, lecturerOne);
	   app.assignLecturer(courseLecture, lecturerTwo);
	   } catch (PreExistException ex) {
		   System.out.println(ex);
	   }
	   
   }
   
 //Test that System should prevent two Staff members being assigned to one Tutorial
   @Test(expected = PreExistException.class)
   public void testPreventAssigningTwoStaffToOneTutorial() throws PreExistException, ClashException {
	   try {
		   CourseOffering offeredCourse = app.createOffering(course1, 100, 2015, 1);
		   Venue courseVenue = app.getVenue("12.10.03");
		   app.assignLecture(offeredCourse, 1, 13.0, 2.0, courseVenue);
		   Tutor tutorOne = app.getTutor("e45777");
		   Tutor tutorTwo = app.getTutor("e45888");
		   Venue tutorialVenueOne = new Venue ("14.09.16", 200, "Tutorial");
		   offeredCourse.addTutorial(2, 17.30, 1.0, tutorialVenueOne);
		   ArrayList<Tutorial> tutorialList = offeredCourse.getTutorials();
		   Tutorial courseTutorialOne = tutorialList.get(0);
		   app.enrolCourse(offeredCourse, student1, "01/04/2015");
		   app.assignTutor(courseTutorialOne, tutorOne);
		   app.assignTutor(courseTutorialOne, tutorTwo);
	   } catch (PreExistException ex) {
		   System.out.println(ex);
	   }
	   
   }
   
 
   
   //Test that System prevents adding course offering without Year and Semester
   @Test(expected = PreExistException.class)
   public void testPreventOfferingWithoutYearSemester() throws PreExistException, ClashException {
	   try {
		   app.createOffering(course2, 100, 0, 0);
	   } catch (PreExistException ex) {
		   System.out.println(ex);
	   }
	   
   }
   
   //Test that System prevents withdrawing from course that student isn't enrolled into
   @Test(expected = PreExistException.class)
   public void testPreventWithdrawalFromCourseWhereNotEnrolled() throws PreExistException, ClashException {
	   try {
	   CourseOffering offeredCourse = app.createOffering(course2, 100, 2015, 1);
	   Venue courseVenue = app.getVenue("12.10.03");
	   app.assignLecture(offeredCourse, 1, 13.0, 2.0, courseVenue);
	   app.withdrawCourse(offeredCourse, student2, "01/04/2015");
	   } catch (PreExistException ex) {
		   System.out.println(ex);
	   }
	   
   }
   
   //Test that System prevents assigning more than one lecture to a course offering
   @Test(expected = PreExistException.class)
   public void testPreventAssignTwoLecturesToOneCourse() throws PreExistException, ClashException {
	   try {
		   CourseOffering offeredCourse = app.createOffering(course2, 100, 2015, 1);
		   Venue courseVenueOne = app.getVenue("12.10.03");
		   Venue courseVenueTwo = new Venue("14.10.12", 200, "Lecture Theater");
		   Venue courseVenueThree = new Venue("14.09.16", 200, "Lecture");
		   app.assignLecture(offeredCourse, 1, 10.0, 2.0, courseVenueOne);
		   app.assignLecture(offeredCourse, 2, 13.0, 2.0, courseVenueTwo);
		   app.assignLecture(offeredCourse, 3, 14.30, 2.0, courseVenueThree);
	   } catch (PreExistException ex) {
		   System.out.println(ex);
	   }
   }
   
   
}
