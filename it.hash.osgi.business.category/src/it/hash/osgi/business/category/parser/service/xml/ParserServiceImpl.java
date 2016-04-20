package it.hash.osgi.business.category.parser.service.xml;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import it.hash.osgi.business.category.AttType;
import it.hash.osgi.business.category.AttValue;
import it.hash.osgi.business.category.Brick;
import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.Cclass;
import it.hash.osgi.business.category.Family;
import it.hash.osgi.business.category.Segment;
import it.hash.osgi.business.category.parser.ParserService;
import it.hash.osgi.business.category.service.CategoryService;

public class ParserServiceImpl implements ParserService {
	private volatile CategoryService _ctgService;

	@Override
	public String getAppCode() {
		// TODO Auto-generated method stub
		return "ctg-prs-xml";
	}

	@Override
	public boolean createCollectionBy(String url, String fileName) {
		URL path = null;
		fileName = url + "\\" + fileName;
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser;
		Map<String, Object> createC;
		boolean response = true;
		try {
			try {
				path = new URL(fileName);
				try {
					URLConnection urlConn = path.openConnection();
					InputStream is = urlConn.getInputStream();
					SAXHandler handler = new SAXHandler();
					parser = parserFactor.newSAXParser();
					parser.parse(is, handler);

					System.out.println(handler.segmentList.size());

					for (Segment segm : handler.segmentList) {

						Category cat = new Category();
						cat.setSegment(segm);
						createC = _ctgService.createCategory(cat);
						// basta che una non vada a buon fine ....si dovrebbe
						// considerare tutta la transazione fallita
						if (createC.get("created").equals(false))
							response = false;
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return response;

	}

}

class SAXHandler extends DefaultHandler {

	List<Segment> segmentList = new ArrayList<>();
	Segment seg = null;

	Family fam = null;

	Cclass clas = null;

	Brick brick = null;

	AttType attType = null;

	AttValue attValue = null;

	String content = null;


	@Override

	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName,

	String qName, Attributes attributes)

	throws SAXException {

		switch (qName) {
		// Create a new Employee object when the start tag is found

		case "segment":

			seg = new Segment();

			seg.setDefinition(attributes.getValue("definition"));
			seg.setCode(Integer.valueOf(attributes.getValue("code")));
			seg.setText(attributes.getValue("text"));

			break;

		case "family":
			fam = new Family();
			fam.setDefinition(attributes.getValue("definition"));
			fam.setCode(Integer.valueOf(attributes.getValue("code")));
			fam.setText(attributes.getValue("text"));
			break;

		case "class":
			clas = new Cclass();
			clas.setDefinition(attributes.getValue("definition"));
			clas.setCode(Integer.valueOf(attributes.getValue("code")));
			clas.setText(attributes.getValue("text"));
			break;

		case "brick":

			brick = new Brick();
			brick.setDefinition(attributes.getValue("definition"));
			brick.setCode(Integer.valueOf(attributes.getValue("code")));
			brick.setText(attributes.getValue("text"));

			break;

		case "attType":
			attType = new AttType();
			attType.setDefinition(attributes.getValue("definition"));
			attType.setCode(Integer.valueOf(attributes.getValue("code")));
			attType.setText(attributes.getValue("text"));

		case "attValue":
			attValue = new AttValue();
			attValue.setDefinition(attributes.getValue("definition"));
			attValue.setCode(Integer.valueOf(attributes.getValue("code")));
			attValue.setText(attributes.getValue("text"));

		}
	}

	@Override

	public void endElement(String uri, String localName,

	String qName) throws SAXException {
		switch (qName) {

		// Add the employee to list once end tag is found

		case "segment":
			segmentList.add(seg);
			break;

		// For all other end tags the employee has to be updated.

		case "family":

			seg.addFamily(fam);
			break;

		case "class":
			fam.addClasse(clas);
			break;

		case "brick":
			clas.addBrick(brick);
			break;

		case "attType":
			brick.addAttType(attType);
			break;
		case "attValue":
			attType.addAttValue(attValue);
			break;
		/*
		 * case "lastName":
		 * 
		 * emp.lastName = content;
		 * 
		 * break;
		 * 
		 * case "location": emp.location = content;
		 * 
		 * break;
		 */

		}

	}

	@Override

	public void characters(char[] ch, int start, int length)

	throws SAXException {

		content = String.copyValueOf(ch, start, length).trim();

	}

}
