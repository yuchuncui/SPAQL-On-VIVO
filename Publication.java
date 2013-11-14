import java.util.ArrayList;

public class Publication {

	private String title;
	private String date;
	private ArrayList<String> authors;

	public Publication(String title, String date, ArrayList<String> authors) {
		this.title = title;
		this.date = date;
		this.authors = authors;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDate() {
		return this.date;
	}

	public ArrayList<String> getAuthors() {
		return this.authors;
	}
}
