import java.util.ArrayList;

public class TestMain {

	public static void main(String args[]) {
		// This is to query with OrcidId, if researcher doesn't have an OrcidId,
		// we can query with his full name
		// ArrayList<String> pubs = QueryUtils.queryOrcidId("0000-0001-6251-9586");
		ArrayList<String> pubs = QueryUtils.queryName("Gloria", "Lee");
		System.out.println("===========================================");
		System.out.println("This person has below listed publications: ");
		System.out.println("-------------------------------------------");
		for (int i = 0; i < pubs.size(); i++) {
			System.out.println(i + 1 + ", " + pubs.get(i));
		}
		System.out.println("===========================================");
		System.out.println("Below lists info about each publication: ");
		System.out.println("-------------------------------------------");
		for (int j = 0; j < pubs.size(); j++) {
			Publication p = QueryUtils.queryPublication(pubs.get(j));
			System.out.println("TITLE: " + p.getTitle());
			System.out.println("DATE: " + p.getDate());
			System.out.print("AUTHORS: ");
			for (int k = 0; k < p.getAuthors().size(); k++) {
				System.out.print(p.getAuthors().get(k) + "/");
			}
			System.out.println("\n-------------------------------------------");
		}
	}
}
