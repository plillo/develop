package it.hash.osgi.business.category.parser.gs1;

import java.io.IOException;
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
import it.hash.osgi.business.category.Clazz;
import it.hash.osgi.business.category.Family;
import it.hash.osgi.business.category.Segment;
import it.hash.osgi.business.category.parser.ParserService;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class ParserServiceImpl extends DefaultHandler implements ParserService {
	private volatile UUIDService _UUIDService;
	private volatile CategoryService _ctgService;
	
	List<Segment> segmentList = new ArrayList<>();
	Segment seg = null;
	Family fam = null;
	Clazz clas = null;
	Brick brick = null;
	AttType attType = null;
	AttValue attValue = null;
	String content = null;
	
	String segmentUUID = null;
	String familyUUID = null;
	String classUUID = null;
	
	@Override
	public String getAppCode() {
		return "ctg-prs-xml";
	}

	@Override
	public boolean addCategoriesByUrl(String url) {
		URL path = null;
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser;
		boolean response = true;
		try {
			try {
				path = new URL(url);
				try {
					URLConnection urlConn = path.openConnection();
					
					// Get parser
					parser = parserFactor.newSAXParser();
					
					// PARSING
					parser.parse(urlConn.getInputStream(), this);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}

		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		}

		return response;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		String definition, code, text;
		Category cat;
		Map<String, Object> result;

		switch (qName) {

		case "segment":
			segmentUUID = _UUIDService.createUUID("bsnss:category");
			definition = attributes.getValue("definition");
			code = attributes.getValue("code");
			text = attributes.getValue("text");
			
			cat = new Category();
			cat.setUuid(segmentUUID);
			cat.setName(text);
			cat.setCode(code);
			cat.set_locDescription(definition);
			result = _ctgService.createCategory(cat);
			if (result.get("created").equals(false))
				segmentUUID = null;
			
			break;

		case "family":
			if(segmentUUID==null)
				break;
			
			familyUUID = _UUIDService.createUUID("bsnss:category");
			definition = attributes.getValue("definition");
			code = attributes.getValue("code");
			text = attributes.getValue("text");
			
			cat = new Category();
			cat.setUuid(familyUUID);
			cat.setName(text);
			cat.setCode(code);
			cat.set_locDescription(definition);
			// set parent UUID
			cat.setParentUuid(segmentUUID);
			result = _ctgService.createCategory(cat);
			if (result.get("created").equals(false))
				familyUUID = null;
			
			break;

		case "class":
			if(familyUUID==null)
				break;
			
			classUUID = _UUIDService.createUUID("bsnss:category");
			definition = attributes.getValue("definition");
			code = attributes.getValue("code");
			text = attributes.getValue("text");
			
			cat = new Category();
			cat.setUuid(classUUID);
			cat.setName(text);
			cat.setCode(code);
			cat.set_locDescription(definition);
			// set parent UUID
			cat.setParentUuid(familyUUID);
			result = _ctgService.createCategory(cat);
			if (result.get("created").equals(false))
				familyUUID = null;
			
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		content = String.copyValueOf(ch, start, length).trim();
	}
}
