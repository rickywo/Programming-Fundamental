public class Tutorial extends Lesson {
	private String label = "";

	public Tutorial(int tutDay, double tutStartHr, double tutDur, Venue venue,
			String label) throws ClashException {
		super(tutDay, tutStartHr, tutDur, venue);
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}