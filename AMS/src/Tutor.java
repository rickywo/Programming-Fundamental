import java.util.ArrayList;

public class Tutor extends Staff {
	ArrayList<Tutorial> tutorials;

	// Administrator can appoint suitable applicants as tutors
	public Tutor(String eNo, String name) {
		super(eNo, name, "Tutor");
		tutorials = new ArrayList<Tutorial>();
	}

	// Administrator can assign tutor to specific tutorials
	public void assign(Tutorial tutorial) throws ClashException,
			PreExistException {
		if (tutorial.getStaff() != null)
			throw new PreExistException("Tutorial is already assigned");
		int i = 0;
		while (i < tutorials.size()) {
			Tutorial next = tutorials.get(i);
			if (next.getDay() == tutorial.getDay()) {
				if (overlap(tutorial.getStart(), tutorial.getEnd(),
						next.getStart(), next.getEnd()))
					throw new ClashException("Tutorial Clash");
				;
			}
			i++;
		}
		tutorials.add(tutorial);
		tutorial.setStaff(this);
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
		return super.toString();
	}

	ArrayList<Tutorial> getTutorials() {
		return tutorials;
	}
}