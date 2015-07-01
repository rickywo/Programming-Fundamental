import java.util.*;

public class Menu {
	String options[];
	Scanner scan = new Scanner(System.in);
	String title;

	public Menu(String title, String options[]) {
		this.options = options;
		this.title = title;
	}

	public void display() {
		System.out.println("\n**************** " + title
				+ " ***********************");
		for (int i = 0; i < options.length; i++)
			System.out.printf("\n%-20s%5d", options[i], i + 1);
		System.out.printf("\n%-20s%5d", "Exit", 0);

		System.out.println("\n*********************************************");
		System.out.print("Your choice : ");
	}

	public int getResponse() {
		int num = -1;
		do {
			display();
			String s;
			try {
				s = scan.nextLine();
				num = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				continue;
			}
		} while (num < 0 || num > options.length);
		return num;
	}
}
