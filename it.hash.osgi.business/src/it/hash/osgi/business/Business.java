
/**
 * Business
 * @author Montinari Antonella
 */
package it.hash.osgi.business;

import static it.hash.osgi.utils.StringUtils.isEmptyOrNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.hash.osgi.geojson.Point;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

/**
 * Pojo attivit�Commerciale
 * 
 * @author Montinari Antonella
 * 
 */

public class Business implements Comparable<Business> {

	@ObjectId
	@Id
	private String _id;
	private String uuid;
	// Mandatory
	private String name;
	// Mandatory
	private String pIva;
	// Mandatory
	private String fiscalCode;
	private String address;
	private String city;
	private String cap;
	private String nation;
	private String _description;
	private String _longDescription;
	private String owner;
	private List<String> categories;
	private List<String> followers;
	private Point position;
	private String logoType;
	private byte[] logo;

	private String email;
	private String mobile;
	private String published;
	private String trusted_email;
	private String trusted_mobile;
	private String cauthor;
	private String cdate;
	private String mauthor;
	private String mdate;
	// look
	private String lauthor;
	private String ldate;
	private Map<String, Object> others;

	public String get_id() {
		return _id;
	}

	public void set_id(String id) {
		this._id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getPIva() {
		return pIva;
	}

	public void setPIva(String pIva) {
		this.pIva = pIva;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCap() {
		return this.cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	/**
	 * gets on description for back end
	 * 
	 * @return short description of the Business
	 */
	public String get__Description() {
		return _description;
	}

	/**
	 * sets on description for back end
	 * 
	 * @param __description
	 *            short description for Business
	 */
	public void set__Description(String _description) {
		this._description = _description;
	}

	/**
	 * gets on description for front end
	 * 
	 * @return long description of the Business
	 */
	public String get__longDescription() {
		return _longDescription;
	}

	/**
	 * sets on description for front end
	 * 
	 * @param __longDescription
	 *            long description for Business
	 */
	public void set__longDescription(String _longDescription) {
		this._longDescription = _longDescription;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		if (this.categories == null)
			this.categories = categories;
		else {
			for (String elem : categories) {
				if (!this.categories.contains(elem))
				    	this.categories.add(elem);
			}
		}
	}

	public boolean addCategory(String category) {
		
		if (this.getCategories() == null)
			categories = new ArrayList<String>();

		if (!this.categories.contains(category))
			 return  this.categories.add(category);
				
		return false;
		
	}

	public boolean removeCategory(String category) {

		return this.categories.remove(category);
	}
	public List<String> getFollowers() {
		return followers;
	}

	public void setFollower(List<String> userUuid) {
		if (this.followers==null)
			this.followers = userUuid;
		else 
		{
			for (String elem:followers){
				if (this.followers.contains(elem))
					this.followers.remove(elem);
				this.followers.add(elem);
			}
		}}
	
	public boolean addFollower(String userUuid) {
		if (this.getFollowers() == null)
			this.followers= new ArrayList<String>();
		if (this.followers.contains(userUuid))
			this.followers.remove(userUuid);
		return this.followers.add(userUuid);
	}

	public boolean removeFollower(String userUuid) {
		if (this.followers.contains(userUuid))
			return this.followers.remove(userUuid);
		else
			return false;
	}
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(Point point) {
		this.position = point;
	}
	
	public void setPosition(Double longitude, Double latitude) {
		this.position = new Point(longitude, latitude);
	}
	
	public String getLogoType() {
		return logoType;
	}

	public void setLogoType(String logoType) {
		this.logoType = logoType;
	}
	
	public byte[] getLogo() {
		return logo;
	}
	
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	
	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPublished() {
		return published;
	}

	/**
	 *
	 * @param published
	 */
	public void setPublished(String published) {
		this.published = published;
	}

	public String getTrusted_email() {
		return trusted_email;
	}

	public void setTrusted_email(String trusted_email) {
		this.trusted_email = trusted_email;
	}

	/**
	 * Gets the name of the author who created the business
	 * 
	 * @return the name of the author
	 */
	public String getCauthor() {
		return cauthor;
	}

	/**
	 * sets name of the author who created the business
	 * 
	 * @param cauthor
	 *            name of the author who created the business
	 */
	public void setCauthor(String cauthor) {
		this.cauthor = cauthor;
	}

	/**
	 * Gets the date on which it was created the business
	 * 
	 * @return the date on which it was created the business
	 */
	public String getCdate() {
		return cdate;
	}

	/**
	 * sets the date on which it was created the business
	 * 
	 * @param cdate
	 *            the date on which it was created the business
	 */
	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	/**
	 * Gets the name of the author who modified the business
	 * 
	 * @return the name of the author
	 */
	public String getMauthor() {
		return mauthor;
	}

	/**
	 * sets name of the author who modified the business
	 * 
	 * @param mauthor
	 *            name of the author who modified the business
	 */
	public void setMauthor(String mauthor) {
		this.mauthor = mauthor;
	}

	/**
	 * Gets the date on which it was modified the business
	 * 
	 * @return the date on which it was modified the business
	 */
	public String getMdate() {
		return mdate;
	}

	/**
	 * sets the date on which it was modified the business
	 * 
	 * @param mdate
	 *            the date on which it was modified the business
	 */
	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	// TODO ....inserire descrizione attributo
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

	/**
	 * Map - OPEN/CLOSE PRINCIPLE: permette di aggiungere nuove variabili di
	 * istanza per analogia ai database Nosql che sono senza schema
	 *
	 * @param others
	 *            - Map<String: name instance variable ; Object: type of the
	 *            instance variable>
	 */
	public void setOthers(Map<String, Object> others) {
		this.others = others;
	}

	/**
	 * Gets una Map contenente attributi dell'entità business
	 * 
	 * @return Map contenente attributi dell'entità business <br>
	 *         che non sono stati previsti al momento della progettazione
	 */
	public Map<String, Object> getOthers() {
		// TODO tradurre in inglese!!!!
		return others;
	}

	/**
	 * Gets eventuali variabili di istanza
	 * 
	 * @return Map contenente variabili di istanza <br>
	 *         che non sono state previste al momento della progettazione
	 */

	public Object addOthers(String attribute, Object o) {
		return this.others.put(attribute, o);
	}

	public Object getOthers(String attribute) {
		return this.others.get(attribute);

	}

	public Object removeOthers(String attribute) {
		return this.others.remove(attribute);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTrusted_mobile() {
		return trusted_mobile;
	}

	public void setTrusted_mobile(String trusted_mobile) {
		this.trusted_mobile = trusted_mobile;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public int compareTo(Business obj) {
		return this._id.compareTo(obj.get_id());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((fiscalCode == null) ? 0 : fiscalCode.hashCode());
		result = prime * result + ((pIva == null) ? 0 : pIva.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Business other = (Business) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (fiscalCode == null) {
			if (other.fiscalCode != null)
				return false;
		} else if (!fiscalCode.equals(other.fiscalCode))
			return false;
		if (pIva == null) {
			if (other.pIva != null)
				return false;
		} else if (!pIva.equals(other.pIva))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public static Map<String, Object> toMap(Business business) {
		if(business==null)
			return null;
		
		Map<String, Object> pars = new HashMap<String, Object>();

		if (!isEmptyOrNull(business.get_id()))
			pars.put("_id", business.get_id());
		if (!isEmptyOrNull(business.getUuid()))
			pars.put("uuid", business.getUuid());
		if (!isEmptyOrNull(business.getName()))
			pars.put("name", business.getName());
		if (!isEmptyOrNull(business.getPIva()))
			pars.put("pIva", business.getPIva());
		if (!isEmptyOrNull(business.getFiscalCode()))
			pars.put("fiscalCode", business.getFiscalCode());
		if (!isEmptyOrNull(business.getAddress()))
			pars.put("address", business.getAddress());
		if (!isEmptyOrNull(business.getCity()))
			pars.put("city", business.getCity());
		if (!isEmptyOrNull(business.getCap()))
			pars.put("cap", business.getCap());
		if (!isEmptyOrNull(business.getNation()))
			pars.put("nation", business.getNation());
		if (!isEmptyOrNull(business.get__Description()))
			pars.put("_description", business.get__Description());
		if (!isEmptyOrNull(business.get__longDescription()))
			pars.put("_longDescription", business.get__longDescription());
		if (!isEmptyOrNull(business.getOwner()))
			pars.put("owner", business.getOwner());
		if (business.getCategories() != null)
			pars.put("categories", business.getCategories());
		if (business.getFollowers()!=null)
			pars.put("followers", business.getFollowers());
		if (business.getPosition()!=null)
			pars.put("position",business.getPosition().toMap());
		if (!isEmptyOrNull(business.getEmail()))
			pars.put("email", business.getEmail());
		if (!isEmptyOrNull(business.getMobile()))
			pars.put("mobile", business.getMobile());
		if (!isEmptyOrNull(business.getLogoType()))
			pars.put("logoType", business.getLogoType());
		if (business.getLogo()!=null)
			pars.put("logo", business.getLogo());
		if (!isEmptyOrNull(business.getPublished()))
			pars.put("published", business.getPublished());
		if (!isEmptyOrNull(business.getTrusted_email()))
			pars.put("trusted_email", business.getTrusted_email());
		if (!isEmptyOrNull(business.getTrusted_mobile()))
			pars.put("trusted_mobile", business.getTrusted_mobile());
		if (!isEmptyOrNull(business.getCauthor()))
			pars.put("cauthor", business.getCauthor());
		if (!isEmptyOrNull(business.getCdate()))
			pars.put("cdate", business.getCdate());
		if (!isEmptyOrNull(business.getMauthor()))
			pars.put("mauthor", business.getMauthor());
		if (!isEmptyOrNull(business.getMdate()))
			pars.put("mdate", business.getMdate());
		if (!isEmptyOrNull(business.getLdate()))
			pars.put("ldate", business.getLdate());
		if (business.getOthers() != null)
			pars.put("others", business.getOthers());

		return pars;
	}
	
	@SuppressWarnings("unchecked")
	public static Business toBusiness(Map<String, Object> mapBusiness) {
		Business business = new Business();
		
		Set<String> entry = mapBusiness.keySet();
		for (Object elem : entry) {
			String attribute = (String) elem;
			if (attribute.equals("_id")) {
				Object o = mapBusiness.get(attribute).toString();
				business.set_id(o.toString());
			} else {
				switch (attribute) {
				case "uuid":
					business.setUuid((String) mapBusiness.get(attribute));
					break;
				case "name":
					business.setName((String) mapBusiness.get(attribute));
					break;
				case "pIva":
					business.setPIva((String) mapBusiness.get(attribute));
					break;
				case "fiscalCode":
					business.setFiscalCode((String) mapBusiness.get(attribute));
					break;
				case "address":
					business.setAddress((String) mapBusiness.get(attribute));
					break;
				case "city":
					business.setCity((String) mapBusiness.get(attribute));
					break;
				case "cap":
					business.setCap((String) mapBusiness.get(attribute));
					break;
				case "nation":
					business.setNation((String) mapBusiness.get(attribute));
					break;
				case "logoType":
					business.setLogoType((String) mapBusiness.get(attribute));
					break;
				case "logo":
					business.setLogo((byte[]) mapBusiness.get(attribute));
					break;
				case "_description":
					business.set__Description((String) mapBusiness.get(elem));
					break;
				case "_longDescription":
					business.set__longDescription((String) mapBusiness.get(elem));
					break;
				case "category":
					business.addCategory((String) mapBusiness.get(elem));
					break;
				case "categories":
					business.setCategories((List<String>) mapBusiness.get(elem));
					break;
				case "followers":
					business.setFollower((List<String>) mapBusiness.get(elem));
					break;
				case "position":
					Map<String, Object> position = (Map<String, Object>)mapBusiness.get("position");
					Map<String, Object> coordinates = (Map<String, Object>)position.get("coordinates");
					business.setPosition(new Point((double)coordinates.get("lat"), (double)coordinates.get("lng")));
				    break;
				case "email":
					business.setEmail((String) mapBusiness.get(elem));
					break;
				case "owner":
					business.setOwner((String) mapBusiness.get(elem));
					break;
				case "mobile":
					business.setMobile((String) mapBusiness.get(elem));
					break;
				case "published":
					business.setPublished((String) mapBusiness.get(elem));
					break;
				case "trusted_email":
					business.setTrusted_email((String) mapBusiness.get(elem));
					break;
				case "trusted_mobile":
					business.setTrusted_mobile((String) mapBusiness.get(elem));
					break;
				case "cauthor":
					business.setCauthor((String) mapBusiness.get(elem));
					break;
				case "cdate":
					business.setCdate((String) mapBusiness.get(elem));
					break;
				case "mauthor":
					business.setMauthor((String) mapBusiness.get(elem));
					break;
				case "mdate":
					business.setMdate((String) mapBusiness.get(elem));
					break;
				case "lauthor":
					business.setLauthor((String) mapBusiness.get(elem));
					break;
				case "ldate":
					business.setLdate((String) mapBusiness.get(elem));
					break;
				case "others":
					if (mapBusiness.get(elem) instanceof Map)
						business.setOthers((Map<String, Object>) mapBusiness.get(elem));
					break;
				default:
					if (business.getOthers() == null)
						business.setOthers(new HashMap<String, Object>());
					if (!business.getOthers().containsKey(attribute))
						business.getOthers().put(attribute, mapBusiness.get(elem));
				}
			}
		}

		return business;
	}
}
