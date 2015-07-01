import java.util.ArrayList;

public class Lecturer extends Staff {
	private String location;
	ArrayList<Lecture> lectures;

	public Lecturer(String eNo, String name, String position, String location) {
		super(eNo, name, position);
		this.location = location;
		lectures = new ArrayList<Lecture>();
	}

	public void assign(Lecture lecture) throws ClashException,
			PreExistException {
		if (lecture.getStaff() != null)
			throw new PreExistException("Lecture is already assigned");
		int i = 0;
		while (i < lectures.size()) {
			Lecture next = lectures.get(i);
			if (next.getDay() == lecture.getDay()) {
				if (overlap(lecture.getStart(), lecture.getEnd(),
						next.getStart(), next.getEnd()))
					throw new ClashException("Lecture Clash");
				;
			}
			i++;
		}
		lectures.add(lecture);
		lecture.setStaff(this);
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

	public String toString() {
		return super.toString() + " Office Loctaion " + location;
	}

	ArrayList<Lecture> getLectures() {
		return lectures;
	}
}