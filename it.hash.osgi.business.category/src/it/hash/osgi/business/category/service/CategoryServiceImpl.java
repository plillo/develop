package it.hash.osgi.business.category.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
//import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.persistence.api.CategoryPersistence;
import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

@SuppressWarnings("unchecked")
@Component(immediate=true)
public class CategoryServiceImpl implements CategoryService {
	// References
	private volatile CategoryPersistence _categoryPersistenceService;
	private volatile AttributeService _attributeService;
	private volatile UuidService _uuidService;
	private volatile EventAdmin _eventAdminService;

	
	@Reference(service=CategoryPersistence.class)
	public void setCategoryPersistence(CategoryPersistence service){
		_categoryPersistenceService = service;
		doLog("CategoryPersistence: "+(service==null?"NULL":"got"));
	}
	
	public void unsetCategoryPersistence(CategoryPersistence service){
		doLog("CategoryPersistence: "+(service==null?"NULL":"released"));
		_categoryPersistenceService = null;
	}
	
	@Reference(service=AttributeService.class)
	public void setAttributeService(AttributeService service){
		_attributeService = service;
		doLog("AttributeService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetAttributeService(AttributeService service){
		doLog("AttributeService: "+(service==null?"NULL":"released"));
		_attributeService = null;
	}
	
	@Reference(service=UuidService.class)
	public void setUuidService(UuidService service){
		_uuidService = service;
		doLog("UuidService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUuidService(UuidService service){
		doLog("UuidService: "+(service==null?"NULL":"released"));
		_uuidService = null;
	}
	
	@Reference(service=EventAdmin.class)
	public void setEventAdmin(EventAdmin service){
		_eventAdminService = service;
		doLog("eventAdminService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetEventAdmin(EventAdmin service){
		_eventAdminService =null;
		doLog("eventAdminService: "+(service==null?"NULL":"released"));
	}
	
	// === end references


	@Override
	public List<Category> getCategory(String search) {
		Map<String, Object> response = new HashMap<String,Object>();
		Map<String,Object> pars = new HashMap<String,Object>();
		if (_uuidService.isUUID(search))
			pars.put("uuid", search);
		else
			if (Category.isCode(search))
				pars.put("code", search);
			else{
				pars.put("name", search);
				}
		
		response =_categoryPersistenceService.getCategory(pars);
		if (response.containsKey("categories"))
				return (List<Category>) response.get("categories") ;
		
		return null;
	}

	
	@Override
	public Category getCategory(Category search) {
		Map<String, Object> response = new HashMap<String,Object>();
		response = _categoryPersistenceService.getCategory(search);
		if (response.containsKey("category"))
				return (Category) response.get("category") ;
		return null;
	}
	
	@Override
	public Category getCategoryByUuid(String uuid) {
		return _categoryPersistenceService.getCategoryByUuid(uuid);
	}

	@Override
	public List<Category> getCategoryByUuid(List<String> uuids) {
		return _categoryPersistenceService.getCategoryByUuid(uuids);
	}

	@Override
	public Map<String, Object> createCategory(Category category) {
		Map<String, Object> response = _categoryPersistenceService.createCategory(category);
		return response ;
	}

	@Override
	public Map<String, Object> updateCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> deleteCategory(String uuid) {
		Dictionary props = new Properties();
		props.put(  EventConstants.EVENT_TOPIC, 
		   "org/eclipse/equinox/events/MemoryEvent/CRITICAL");	
		props.put("uuidCategory", uuid);
		Event event= new Event("delete",props);
		_eventAdminService.postEvent(event);
		
		Map<String,Object> tmp= new HashMap<String,Object>();
		return tmp;
//	return _categoryPersistenceService.deleteCategory(uuid);
	}

	@Override
	public Map<String, Object> createCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> retrieveCategories(String criterion, String search) {
		if (criterion==null)
			return getCategory(search);
		else{
			if (_uuidService.isUUID(search))
				criterion="uuid";
			else
				if (Category.isCode(search))
					criterion="code";
				else
                     criterion="name";
			
			 return _categoryPersistenceService.retrieveCategories(criterion,search);
		}
		
	    
	}

	// ATTRIBUTES
	// ==========
	@Override
	public List<Attribute> getAttributes(String ctgUuid) {
		List<String> listS= new ArrayList<String>();
		String[] cat = ctgUuid.split(",");
		for(String cat1: cat){
			listS.add(cat1);
		}
	
		return _attributeService.getAttributesByCategories(listS);
	}

	@Override
	public Map<String, Object> createAttribute(String ctgUuid, Attribute attribute) {
		if (attribute.getApplications() == null)
			attribute.setApplications(new ArrayList<Map<String,Object>>());

		/*
		List<Map<String,Object>> applications = attribute.getApplications();
		if(applications!= null && !applications.contains("busctg:" + ctgUuid))
			attribute.add("busctg:" + ctgUuid);
		*/

		return _attributeService.createAttribute(attribute);
	}

	@Override
	public Map<String, Object> updateAttribute(String ctgUuid, Attribute attr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteAttribute(String ctgUuid, String attrUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createCollectionByCsv(String url, String fileName) {
		URL path = null;
		fileName = url + "\\" + fileName;
		boolean response = true;
		try {
			path = new URL(fileName);
			URLConnection urlConn = path.openConnection();

			//String outputString = null;

			String line;

			//outputString = "";
			BufferedReader readerFile = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF8"));

			Map<String, Object> createC;

			String[] doc;

			line = readerFile.readLine();
			int i = 1;
			while (line != null) {

				doc = line.split(";");
				Category cat = new Category();
				cat.setCode(doc[0]);
				cat.setName(doc[1]);
				createC = createCategory(cat);
				// basta che una non vada a buon fine ....si dovrebbe
				// considerare tutta la transazione fallita
				if (createC.get("created").equals(false))
					response = false;
				line = readerFile.readLine();
				i++;
				if (i == 1987)
					System.out.println(" trovato");
			}
			readerFile.close();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}


	@SuppressWarnings("unused")
	private String is(String search) {
		// TODO Auto-generated method stub

		String is ;
		
		if (_uuidService.isUUID(search)) {
			is="uuid";}
		else {
			if (Category.isCode(search)) {
				is="code";}
			else {
				is="name";
			}
		}

		return is;
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
