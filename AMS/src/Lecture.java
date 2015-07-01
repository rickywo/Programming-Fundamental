public class Lecture extends Lesson {

	public Lecture(int lecDay, double lecStartHr, double lecDur, Venue venue)
			throws ClashException {
		super(lecDay, lecStartHr, lecDur, venue);
	}
}