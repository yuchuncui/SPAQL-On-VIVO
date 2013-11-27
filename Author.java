public class Author {

	public String lastName;
	public String firstName;
	public String middleName;
	public boolean isSunyReach;

	public Author(String lastName, String firstName, boolean b) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = "";
		this.isSunyReach = b;
	}
	
	public Author(String lastName, String firstName, String middleName, boolean b) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.isSunyReach = b;
	}
	
	public void setIsSunyReach(boolean b) {
		this.isSunyReach = b;
	}
}
