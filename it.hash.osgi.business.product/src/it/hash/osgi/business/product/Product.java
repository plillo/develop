package it.hash.osgi.business.product;

import static it.hash.osgi.utils.StringUtils.isEmptyOrNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class Product implements Comparable<Product> {
	 
	@ObjectId @Id
	private String _id;
	private String uuid;
	private String business;
	private List<String> categories;
	private List<String> stores;
	private List<String> pictures;
	private String code;
	private String barcode;
	private String _locDescription;
	private String _locLongDescription;
	private List<Double> prices;
	private String cauthor;
	private String cdate;
	private String mauthor;
	private String mdate;
	private String lauthor;
	private String ldate;
	private Map<String, Object> others;
	
	public String get_id() {
		return _id;
	}

	public void set_id(String id) {
		this._id = id;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public void addCategory(String category) {
		if(this.categories==null)
			this.categories = new Vector<String>();
		
		this.categories.add(category);
	}
	
	public void removeCategory(String category) {
		if(this.categories==null)
			return;
		
		this.categories.remove(category);
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}
	
	public void addPicture(String picture) {
		if(this.pictures==null)
			this.pictures = new Vector<String>();
		
		this.pictures.add(picture);
	}
	
	public void removePicture(String picture) {
		if(this.pictures==null)
			return;
		
		this.pictures.remove(picture);
	}
	
	public List<String> getStores() {
		return stores;
	}

	public void setStores(List<String> stores) {
		this.stores = stores;
	}
	
	public void addStore(String store) {
		if(this.stores==null)
			this.stores = new Vector<String>();
		
		this.stores.add(store);
	}
	
	public void removeStore(String store) {
		if(this.stores==null)
			return;
		
		this.stores.remove(store);
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String get_locDescription() {
		return _locDescription;
	}
	public void set_locDescription(String _locDescription) {
		this._locDescription = _locDescription;
	}
	
	public String get_locLongDescription() {
		return _locLongDescription;
	}
	
	public void set_locLongDescription(String _locLongDescription) {
		this._locLongDescription = _locLongDescription;
	}
	
	public List<Double> getPrices() {
		return prices;
	}

	public void setPrices(List<Double> prices) {
		this.prices = prices;
	}

	public String getCauthor() {
		return cauthor;
	}

	public void setCauthor(String cauthor) {
		this.cauthor = cauthor;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getMauthor() {
		return mauthor;
	}

	public void setMauthor(String mauthor) {
		this.mauthor = mauthor;
	}

	public String getMdate() {
		return mdate;
	}

	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	public String getLauthor() {
		return lauthor;
	}

	public void setLauthor(String lauthor) {
		this.lauthor = lauthor;
	}

	public String getLdate() {
		return ldate;
	}

	public void setLdate(String ldate) {
		this.ldate = ldate;
	}
	
	public void setOthers(Map<String, Object> others) {
		this.others = others;
	}
	
	public Map<String, Object> getOthers() {
		return others;
	}
	
	public Object addOthers(String attribute, Object o) {
		return this.others.put(attribute, o);
	}

	public Object getOthers(String attribute) {
		return this.others.get(attribute);

	}

	public Object removeOthers(String attribute) {
		return this.others.remove(attribute);
	}
	
	public static Map<String, Object> toMap(Product item) {
		if(item==null)
			return null;
		
		Map<String, Object> pars = new HashMap<String, Object>();

		if (!isEmptyOrNull(item.get_id()))
			pars.put("_id", item.get_id());
		if (!isEmptyOrNull(item.getUuid()))
			pars.put("uuid", item.getUuid());
		if (!isEmptyOrNull(item.getCode()))
			pars.put("code", item.getCode());
		if (!isEmptyOrNull(item.getBarcode()))
			pars.put("barcode", item.getBarcode());
		if (!isEmptyOrNull(item.get_locDescription()))
			pars.put("_locDescription", item.get_locDescription());
		if (!isEmptyOrNull(item.get_locLongDescription()))
			pars.put("_locLongDescription", item.get_locLongDescription());
		if (!isEmptyOrNull(item.getBusiness()))
			pars.put("business", item.getBusiness());
		
		// Lists
		if (item.getCategories()!=null)
			pars.put("categories", item.getCategories());
		if (item.getStores()!=null)
			pars.put("stores", item.getStores());
		if (item.getPrices()!=null)
			pars.put("prices", item.getPrices());
		if (item.getPictures()!=null)
			pars.put("pictures", item.getPictures());
		
		// Authoring
		if (!isEmptyOrNull(item.getCauthor()))
			pars.put("cauthor", item.getCauthor());
		if (!isEmptyOrNull(item.getCdate()))
			pars.put("cdate", item.getCdate());
		if (!isEmptyOrNull(item.getMauthor()))
			pars.put("mauthor", item.getMauthor());
		if (!isEmptyOrNull(item.getMdate()))
			pars.put("mdate", item.getMdate());
		if (!isEmptyOrNull(item.getLdate()))
			pars.put("ldate", item.getLdate());
		
		// Others
		if (item.getOthers() != null)
			pars.put("others", item.getOthers());

		return pars;
	}
	
	@SuppressWarnings("unchecked")
	public static Product toProduct(Map<String, Object> map) {
		Product item = new Product();
		
		Set<String> entry = map.keySet();
		for (Object elem : entry) {
			String attribute = (String) elem;
			if (attribute.equals("_id")) {
				Object o = map.get(attribute).toString();
				item.set_id(o.toString());
			} else {
				switch (attribute) {
				case "uuid":
					item.setUuid((String) map.get(attribute));
					break;
				case "business":
					item.setBusiness((String) map.get(attribute));
					break;
				case "code":
					item.setCode((String) map.get(attribute));
					break;
				case "barcode":
					item.setBarcode((String) map.get(attribute));
					break;
				case "_locDescription":
					item.set_locDescription((String) map.get(attribute));
					break;
				case "_locLongDescription":
					item.set_locLongDescription((String) map.get(attribute));
					break;
				case "categories":
					item.setCategories((List<String>) map.get(elem));
					break;
				case "stores":
					item.setStores((List<String>) map.get(elem));
					break;
				case "prices":
					item.setPrices((List<Double>) map.get(elem));
					break;
				case "pictures":
					item.setPictures((List<String>) map.get(elem));
					break;
				case "cauthor":
					item.setCauthor((String) map.get(elem));
					break;
				case "cdate":
					item.setCdate((String) map.get(elem));
					break;
				case "mauthor":
					item.setMauthor((String) map.get(elem));
					break;
				case "mdate":
					item.setMdate((String) map.get(elem));
					break;
				case "lauthor":
					item.setLauthor((String) map.get(elem));
					break;
				case "ldate":
					item.setLdate((String) map.get(elem));
					break;
				case "others":
					if (map.get(elem) instanceof Map)
						item.setOthers((Map<String, Object>) map.get(elem));
					break;
				default:
					if (item.getOthers() == null)
						item.setOthers(new HashMap<String, Object>());
					if (!item.getOthers().containsKey(attribute))
						item.getOthers().put(attribute, map.get(elem));
				}
			}
		}

		return item;
	}

	@Override
	public int compareTo(Product obj) {
		return this._id.compareTo(obj.get_id());
	}
}
