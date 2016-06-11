package it.hash.osgi.business.category.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.persistence.api.CategoryPersistence;
import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

@SuppressWarnings("unchecked")
public class CategoryServiceImpl implements CategoryService {
	// Injected services
	private volatile CategoryPersistence _persistenceSrv;
	private volatile UuidService _uuidSrv;
	private volatile AttributeService _attributeSrv;

	@Override
	public List<Category> getCategory(String search) {
		Map<String, Object> response = new HashMap<String,Object>();
		Map<String,Object> pars = new HashMap<String,Object>();
		if (_uuidSrv.isUUID(search))
			pars.put("uuid", search);
		else
			if (Category.isCode(search))
				pars.put("code", search);
			else{
				pars.put("name", search);
				}
		
		response =_persistenceSrv.getCategory(pars);
		if (response.containsKey("categories"))
				return (List<Category>) response.get("categories") ;
		
		return null;
	}

	
	@Override
	public Category getCategory(Category search) {
		Map<String, Object> response = new HashMap<String,Object>();
		response = _persistenceSrv.getCategory(search);
		if (response.containsKey("category"))
				return (Category) response.get("category") ;
		return null;
	}
	
	@Override
	public Category getCategoryByUuid(String uuid) {
		return _persistenceSrv.getCategoryByUuid(uuid);
	}

	@Override
	public List<Category> getCategoryByUuid(List<String> uuids) {
		return _persistenceSrv.getCategoryByUuid(uuids);
	}

	@Override
	public Map<String, Object> createCategory(Category category) {
		Map<String, Object> response = _persistenceSrv.createCategory(category);
		return response ;
	}

	@Override
	public Map<String, Object> updateCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteCategory(String uuid) {
		return _persistenceSrv.deleteCategory(uuid);
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
			if (_uuidSrv.isUUID(search))
				criterion="uuid";
			else
				if (Category.isCode(search))
					criterion="code";
				else
                     criterion="name";
			
			 return _persistenceSrv.retrieveCategories(criterion,search);
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
	
		return _attributeSrv.getAttributesByCategories(listS);
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

		return _attributeSrv.createAttribute(attribute);
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
		
		if (_uuidSrv.isUUID(search)) {
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




}
