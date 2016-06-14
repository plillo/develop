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

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import it.hash.osgi.business.category.AttributeType;
import it.hash.osgi.business.category.AttributeValue;
import it.hash.osgi.business.category.Brick;
import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.Clazz;
import it.hash.osgi.business.category.Family;
import it.hash.osgi.business.category.Segment;
import it.hash.osgi.business.category.parser.ParserService;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.resource.uuid.api.UuidService;

@Component(immediate=true)
public class ParserServiceImpl extends DefaultHandler implements ParserService {
	List<Segment> segmentList = new ArrayList<>();
	Segment seg = null;
	Family fam = null;
	Clazz clas = null;
	Brick brick = null;
	AttributeType attType = null;
	AttributeValue attValue = null;
	String content = null;
	
	String segmentUUID = null;
	String familyUUID = null;
	String classUUID = null;
	
	// References
	private UuidService _UUIDService;
	private CategoryService _categoryService;
	
	@Reference(service=UuidService.class)
	public void setUuidService(UuidService service){
		_UUIDService = service;
		doLog("UuidService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUuidService(UuidService service){
		doLog("UuidService: "+(service==null?"NULL":"released"));
		_UUIDService = null;
	}
	
	@Reference(service=CategoryService.class)
	public void setCategoryService(CategoryService service){
		_categoryService = service;
		doLog("CategoryService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetCategoryService(CategoryService service){
		doLog("CategoryService: "+(service==null?"NULL":"released"));
		_categoryService = null;
	}
	
	@Override
	public String getParserCode() {
		return "gs1-xml";
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
			result = _categoryService.createCategory(cat);
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
			result = _categoryService.createCategory(cat);
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
			result = _categoryService.createCategory(cat);
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
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
