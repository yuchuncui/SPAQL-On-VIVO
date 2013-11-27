import java.util.ArrayList;

public class Publication {

	private String title;
	private String date;
	private ArrayList<Author> authors;
	private ArrayList<String> authorNames;
	private String startPage;
	private String endPage;
	private String publishedIn;
	private String volume;
	private String DOI;

	public Publication(String title, String date, ArrayList<String> authorNames) {
		this.title = title;
		this.date = date;
		this.authorNames = authorNames;
	}

	public Publication(String title, String date, ArrayList<Author> authors, String startPage, String endPage,
			String publishedIn, String volume, String DOI) {
		this.title = title;
		this.date = date;
		this.authors = authors;
		this.startPage = startPage;
		this.endPage = endPage;
		this.publishedIn = publishedIn;
		this.volume = volume;
		this.DOI = DOI;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDate() {
		return this.date;
	}

	public ArrayList<Author> getAuthors() {
		return this.authors;
	}
	
	public ArrayList<String> getAuthorNames() {
		return this.authorNames;
	}
	
	public String getEndPage() {
		return this.endPage;
	}
	
	public String getStartPage() {
		return this.startPage;
	}
	
	public String getVolumn() {
		return this.volume;
	}
	
	public String getDOI() {
		return this.DOI;
	}
	
	public String getPublishedIn() {
		return this.publishedIn;
	}
}
