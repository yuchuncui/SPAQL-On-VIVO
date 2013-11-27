import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.pfunction.library.seq;

public class OutputRDF {

	public static void printPubRDF(Publication pub, String sunyreachPubCode, String format) {
		Model model = ModelFactory.createDefaultModel();
		String geo = "http://aims.fao.org/aos/geopolitical.owl#";
		model.setNsPrefix("geo", geo);
		String c4o = "http://purl.org/spar/c4o/";
		model.setNsPrefix("c4o", c4o);
		String vitro_public = "http://vitro.mannlib.cornell.edu/ns/vitro/public#";
		model.setNsPrefix("vitro-public", vitro_public);
		String skos = "http://www.w3.org/2004/02/skos/core#";
		model.setNsPrefix("skos", skos);
		String ero = "http://purl.obolibrary.org/obo/";
		model.setNsPrefix("ero", ero);
		String event = "http://purl.org/NET/c4dm/event.owl#";
		model.setNsPrefix("event", event);
		String pvs = "http://vivoweb.org/ontology/provenance-support#";
		model.setNsPrefix("pvs", pvs);
		String dcelem = "http://purl.org/dc/elements/1.1/";
		model.setNsPrefix("dcelem", dcelem);
		String vivo = "http://vivoweb.org/ontology/core#";
		model.setNsPrefix("vivo", vivo);
		String vitro = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#";
		model.setNsPrefix("vitro", vitro);
		String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		model.setNsPrefix("rdf", rdf);
		String bibo = "http://purl.org/ontology/bibo/";
		model.setNsPrefix("bibo", bibo);
		String foaf = "http://xmlns.com/foaf/0.1/";
		model.setNsPrefix("foaf", foaf);
		String reach = "http://reach.suny.edu/ontology/core#";
		model.setNsPrefix("reach", reach);
		String owl = "http://www.w3.org/2002/07/owl#";
		model.setNsPrefix("owl", owl);
		String dcterms = "http://purl.org/dc/terms/";
		model.setNsPrefix("dcterms", dcterms);
		String scires = "http://vivoweb.org/ontology/scientific-research#";
		model.setNsPrefix("scires", scires);
		String fabio = "http://purl.org/spar/fabio/";
		model.setNsPrefix("fabio", fabio);
		String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
		model.setNsPrefix("rdfs", rdfs);

		Property type = model.createProperty(rdf + "type");
		Property label = model.createProperty(rdfs + "label");
		Property pageEnd = model.createProperty(bibo + "pageEnd");
		Property pageStart = model.createProperty(bibo + "pageStart");
		Property volume = model.createProperty(bibo + "volume");
		Property doi = model.createProperty(bibo + "doi");
		Property dateTimeValue = model.createProperty(vivo + "dateTimeValue");
		Property hasPublicationVenue = model.createProperty(vivo + "hasPublicationVenue");
		Property informationResourceInAuthorship = model.createProperty(vivo + "informationResourceInAuthorship");
		Property linkedAuthor = model.createProperty(vivo + "linkedAuthor");
		Property linkedInformationResource = model.createProperty(vivo + "linkedInformationResource");
		Property dateTime = model.createProperty(vivo + "dateTime");
		Property dateTimePrecision = model.createProperty(vivo + "dateTimePrecision");
		Property title = model.createProperty(dcterms + "volume");

		Resource owlThingResource = model.createResource("http://www.w3.org/2002/07/owl#Thing");
		Resource root = model.createResource("http://reach.suny.edu/individual/" + sunyreachPubCode);
		Resource publicationResource = model.createResource("http://reach.suny.edu/individual/" + pub.getPublishedIn());
		Resource dateResource = model.createResource("http://reach.suny.edu/individual/year" + pub.getDate());
		Resource[] authorshipResource = new Resource[pub.getAuthors().size()];

		for (int i = 0; i < pub.getAuthors().size(); i++) {
			authorshipResource[i] = model.createResource("http://reach.suny.edu/individual/g"
					+ pub.getAuthors().get(i).lastName + "_" + pub.getAuthors().get(i).firstName + "_"
					+ sunyreachPubCode.substring(1));
			Resource authorResource = model.createResource("http://reach.suny.edu/individual/"
					+ pub.getAuthors().get(i).lastName + "_" + pub.getAuthors().get(i).firstName);
			root.addProperty(informationResourceInAuthorship, authorshipResource[i]);
			authorshipResource[i].addProperty(type, owlThingResource)
					.addProperty(type, model.createResource("http://vivoweb.org/ontology/core#Relationship"))
					.addProperty(type, model.createResource("http://vivoweb.org/ontology/core#Authorship"))
					.addProperty(linkedAuthor, authorResource).addProperty(linkedInformationResource, root);
			if (pub.getAuthors().get(i).isSunyReach) {
				authorResource.addProperty(label, model.createLiteral(pub.getAuthors().get(i).lastName + ", "
						+ pub.getAuthors().get(i).firstName, "en-US"));
			} else {
				String middleNameAbbr = pub.getAuthors().get(i).middleName.equals("") ? ""
						: pub.getAuthors().get(i).middleName.substring(0, 1);
				authorResource.addProperty(label, pub.getAuthors().get(i).lastName + " "
						+ pub.getAuthors().get(i).firstName.substring(0, 1) + middleNameAbbr);
			}
		}

		root.addProperty(label, pub.getTitle()).addProperty(pageEnd, pub.getEndPage())
				.addProperty(dateTimeValue, dateResource).addProperty(pageStart, pub.getStartPage())
				.addProperty(volume, pub.getVolumn()).addProperty(title, pub.getTitle()).addProperty(doi, pub.getDOI())
				.addProperty(hasPublicationVenue, publicationResource)
				.addProperty(type, model.createResource("http://purl.org/ontology/bibo/Document"))
				.addProperty(type, model.createResource("http://vivoweb.org/ontology/core#InformationResource"))
				.addProperty(type, model.createResource("http://purl.org/ontology/bibo/Article"))
				.addProperty(type, owlThingResource);

		XSDDatatype dt = new XSDDatatype("dateTime");
		Literal l1 = model.createTypedLiteral(pub.getDate() + "-01-01T00:00:00", dt);
		Literal l2 = model.createTypedLiteral(pub.getDate() + "-01-01T05:00:00Z", dt);
		dateResource
				.addProperty(type, owlThingResource)
				.addProperty(type, model.createResource("http://vivoweb.org/ontology/core#DateTimeValue"))
				.addLiteral(dateTime, l1)
				.addLiteral(dateTime, l2)
				.addProperty(
						dateTimePrecision,
						model.createResource("http://vivoweb.org/ontology/core#yearPrecision").addProperty(label,
								model.createLiteral("yearPrecision", "en-US")));
		publicationResource.addProperty(label, pub.getPublishedIn().replace(' ', '-'));

		try {
			OutputStream os = null;
			if (format.equals("rdf")) {
				os = new FileOutputStream(sunyreachPubCode + ".rdf");
				model.write(os);
				System.out.println("File " + sunyreachPubCode + ".rdf generated.");
			} else if (format.equals("nt")) {
				os = new FileOutputStream(sunyreachPubCode + ".nt");
				model.write(os, "N-TRIPLE");
				System.out.println("File " + sunyreachPubCode + ".nt generated.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void addResearcherRDF(Author author, Publication pub, String sunyreachPubCode, String format) {
		Model model = ModelFactory.createDefaultModel();
		String geo = "http://aims.fao.org/aos/geopolitical.owl#";
		model.setNsPrefix("geo", geo);
		String c4o = "http://purl.org/spar/c4o/";
		model.setNsPrefix("c4o", c4o);
		String vitro_public = "http://vitro.mannlib.cornell.edu/ns/vitro/public#";
		model.setNsPrefix("vitro-public", vitro_public);
		String skos = "http://www.w3.org/2004/02/skos/core#";
		model.setNsPrefix("skos", skos);
		String ero = "http://purl.obolibrary.org/obo/";
		model.setNsPrefix("ero", ero);
		String event = "http://purl.org/NET/c4dm/event.owl#";
		model.setNsPrefix("event", event);
		String pvs = "http://vivoweb.org/ontology/provenance-support#";
		model.setNsPrefix("pvs", pvs);
		String dcelem = "http://purl.org/dc/elements/1.1/";
		model.setNsPrefix("dcelem", dcelem);
		String vivo = "http://vivoweb.org/ontology/core#";
		model.setNsPrefix("vivo", vivo);
		String vitro = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#";
		model.setNsPrefix("vitro", vitro);
		String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		model.setNsPrefix("rdf", rdf);
		String bibo = "http://purl.org/ontology/bibo/";
		model.setNsPrefix("bibo", bibo);
		String foaf = "http://xmlns.com/foaf/0.1/";
		model.setNsPrefix("foaf", foaf);
		String reach = "http://reach.suny.edu/ontology/core#";
		model.setNsPrefix("reach", reach);
		String owl = "http://www.w3.org/2002/07/owl#";
		model.setNsPrefix("owl", owl);
		String dcterms = "http://purl.org/dc/terms/";
		model.setNsPrefix("dcterms", dcterms);
		String scires = "http://vivoweb.org/ontology/scientific-research#";
		model.setNsPrefix("scires", scires);
		String fabio = "http://purl.org/spar/fabio/";
		model.setNsPrefix("fabio", fabio);
		String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
		model.setNsPrefix("rdfs", rdfs);

		Property type = model.createProperty(rdf + "type");
		Property label = model.createProperty(rdfs + "label");
		Property linkedAuthor = model.createProperty(vivo + "linkedAuthor");
		Property linkedInformationResource = model.createProperty(vivo + "linkedInformationResource");

		Resource root = model.createResource("http://reach.suny.edu/individual/g" + author.lastName + "_"
				+ author.firstName + "_" + sunyreachPubCode.substring(1));

		root.addProperty(type, model.createResource("http://vivoweb.org/ontology/core#Relationship"))
				.addProperty(type, model.createResource("http://vivoweb.org/ontology/core#Authorship"))
				.addProperty(type, model.createResource("http://www.w3.org/2002/07/owl#Thing"))
				.addProperty(
						linkedAuthor,
						model.createResource("http://reach.suny.edu/individual/" + author.lastName + "_"
								+ author.firstName))
				.addProperty(
						linkedInformationResource,
						model.createResource("http://reach.suny.edu/individual/" + sunyreachPubCode).addProperty(label,
								pub.getTitle()));
		
		try {
			OutputStream os = null;
			if (format.equals("rdf")) {
				os = new FileOutputStream(author.lastName + "_" + author.firstName + "_add.rdf");
				model.write(os);
				System.out.println("File " + author.lastName + "_" + author.firstName + "_add.rdf generated");
			} else if (format.equals("nt")) {
				os = new FileOutputStream(author.lastName + "_" + author.firstName + "_add.nt");
				model.write(os, "N-TRIPLE");
				System.out.println("File " + author.lastName + "_" + author.firstName + "_add.nt generated");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		ArrayList<Author> authors = new ArrayList<>();
		String firstName = "Janos";
		String lastName = "Hajagos";
		String middleName = "";
		Author a = new Author(lastName, firstName, middleName, QueryUtils.isSunyReachMember(firstName, lastName));
		authors.add(a);
		String title = "Emerging practices for mapping and linking life sciences data using RDF - A case";
		String publishedIn = "Journal of Web Semantics";
		String DOI = "10.1016/j.websem.2012.02.003";
		Publication p = new Publication(title, "2012", authors, "2", "13", publishedIn, "14", DOI);

		OutputRDF.printPubRDF(p, "a123456788", "rdf");
		OutputRDF.printPubRDF(p, "a123456788", "nt");
		OutputRDF.addResearcherRDF(a, p, "a123456788", "rdf");
		OutputRDF.addResearcherRDF(a, p, "a123456788", "nt");
	}
}
