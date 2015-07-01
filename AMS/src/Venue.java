import java.util.ArrayList;

public class Venue {
	private String location;
	private int capacity;
	private String purpose;
	private ArrayList<Lesson> lessons;

	public Venue(String location, int capacity, String purpose) {
		this.location = location;
		this.capacity = capacity;
		this.purpose = purpose;
		lessons = new ArrayList<Lesson>();
	}

	public ArrayList<Lesson> getLessons() {
		return lessons;
	}

	public void addLesson(Lesson lesson) {
		lessons.add(lesson);
	}

	public void checkClash(int day, double start, double end)
			throws ClashException {

		for (int i = 0; i < lessons.size(); i++) {
			if (day == lessons.get(i).getDay())
				if (overlap(start, end, lessons.get(i).getStart(),
						lessons.get(i).getEnd()))
					throw new ClashException("Venue Clash");
		}
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

	public String getLocation() {
		return location;
	}

	public int getCapacity() {
		return capacity;
	}

	public String toString() {
		return "Location " + location + " Capacity " + capacity + " Purpose "
				+ purpose;
	}
}