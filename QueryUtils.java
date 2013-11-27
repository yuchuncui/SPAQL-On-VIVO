import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

public class QueryUtils {

	private static String source = "http://link.informatics.stonybrook.edu/sparql/";
	
	public static boolean isSunyReachMember(String firstName, String lastName) {
		String queryByName1 = 
		        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
			"SELECT ?author WHERE { " +
		        "?author foaf:lastName \"" + lastName + "\" ." +
			"?author foaf:firstName \"" + firstName + "\" ."+
			"}";
		QueryExecution qexec = QueryExecutionFactory.sparqlService(source, QueryFactory.create(queryByName1));
		ResultSet results = qexec.execSelect();
		qexec.close();
		if (results.hasNext()) {
			return true;
		} else {
			String queryByName2 =
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
				"SELECT ?author WHERE { " +
				"?author foaf:lastName \"" + lastName + "\"^^<http://www.w3.org/2001/XMLSchema#string> ." +
				"?author foaf:firstName \"" + firstName + "\"^^<http://www.w3.org/2001/XMLSchema#string> ."+
				"}";	
			qexec = QueryExecutionFactory.sparqlService(source, QueryFactory.create(queryByName2));
			results = qexec.execSelect();
			qexec.close();
			if (results.hasNext()) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> queryOrcidId(String orcidId) {
		String queryByOrcid = 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + 
		        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " + 
			"PREFIX vivo: <http://vivoweb.org/ontology/core#> " +
			"SELECT ?pub WHERE { " +
		        "?author <http://vivoweb.org/ontology/core#authorInAuthorship> ?authorship ." +
			"?resource <http://vivoweb.org/ontology/core#informationResourceInAuthorship> ?authorship ." +
		        "?author vivo:orcidId \"" + orcidId + "\" ." +
			"?resource rdfs:label ?pub ."+
		        "}";
		QueryExecution qexec = QueryExecutionFactory.sparqlService(source, QueryFactory.create(queryByOrcid));
		ResultSet results = qexec.execSelect();
		// ResultSetFormatter.out(System.out, results, query);
		qexec.close();
		ArrayList<String> pubs = new ArrayList<String>();
		while (results.hasNext()) {
			String temp = results.next().toString();
			pubs.add(temp.substring(10, temp.length() - 3));
		}
		return pubs;
	}

	public static ArrayList<String> queryName(String firstName, String lastName) {
		ArrayList<String> pubs = new ArrayList<String>();
		String queryByName1 = 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + 
		        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " + 
			 "PREFIX vivo: <http://vivoweb.org/ontology/core#> " +
			"SELECT ?pub WHERE { " +
		        "?author <http://vivoweb.org/ontology/core#authorInAuthorship> ?authorship ." +
			"?resource <http://vivoweb.org/ontology/core#informationResourceInAuthorship> ?authorship ." +
		        "?author foaf:lastName \"" + lastName + "\" ." +
			"?author foaf:firstName \"" + firstName + "\" ."+
		        "?resource rdfs:label ?pub ." +
			"}";
		QueryExecution qexec = QueryExecutionFactory.sparqlService(source, QueryFactory.create(queryByName1));
		ResultSet results = qexec.execSelect();
		qexec.close();
		if (!results.hasNext()) {
			String queryByName2 =
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " + 
				"PREFIX vivo: <http://vivoweb.org/ontology/core#> " +
				"SELECT ?pub WHERE { " +
				"?author <http://vivoweb.org/ontology/core#authorInAuthorship> ?authorship ." +
				"?resource <http://vivoweb.org/ontology/core#informationResourceInAuthorship> ?authorship ." +
				"?author foaf:lastName \"" + lastName + "\"^^<http://www.w3.org/2001/XMLSchema#string> ." +
				"?author foaf:firstName \"" + firstName + "\"^^<http://www.w3.org/2001/XMLSchema#string> ."+
				"?resource rdfs:label ?pub ." +
				"}";	
			qexec = QueryExecutionFactory.sparqlService(source, QueryFactory.create(queryByName2));
			results = qexec.execSelect();
			qexec.close();
		}
		
		while (results.hasNext()) {
			String temp = results.next().toString();
			pubs.add(temp.substring(10, temp.length() - 3));
		}
		return pubs;
	}

	public static Publication queryPublication(String pub) {
		String queryDate = 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " + 
			"PREFIX vivo: <http://vivoweb.org/ontology/core#> " +
		        "SELECT ?year WHERE { " +
			"?authorship vivo:dateTimeValue ?date ;" +
		        "vivo:informationResourceInAuthorship ?authors ;" +
		        "rdfs:label \"" + pub + "\" ." +
			"?date vivo:dateTime ?year ." +
		        "}LIMIT 1";
		
		String queryAuthors = 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
			"PREFIX vivo: <http://vivoweb.org/ontology/core#> " +
		        "SELECT ?name WHERE { " + 
			"?author vivo:authorInAuthorship ?authorship ." +
			"?resource vivo:informationResourceInAuthorship ?authorship ." +
		        "?author rdfs:label ?name ." +
			"?resource rdfs:label \"" + pub + "\" ." +
		        "}";
		
		QueryExecution qexec1 = QueryExecutionFactory.sparqlService(source, QueryFactory.create(queryDate));
		ResultSet results1 = qexec1.execSelect();
		qexec1.close();
		String year = results1.next().toString().substring(11, 15);
		QueryExecution qexec2 = QueryExecutionFactory.sparqlService(source, QueryFactory.create(queryAuthors));
		ResultSet results2 = qexec2.execSelect();
		qexec2.close();
		ArrayList<String> authors = new ArrayList<String>();
		while (results2.hasNext()) {
			String temp = results2.next().toString();
			authors.add(temp.substring(11, temp.length() - 3));
		}
		return new Publication(pub, year, authors);
	}
}
