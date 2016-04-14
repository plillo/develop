
/**
 * Business
 * @author Montinari Antonella
 */
package it.hash.osgi.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.hash.osgi.geoJson.Point;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

/**
 * Pojo attività Commerciale
 * 
 * @author Montinari Antonella
 * 
 */

public class Business implements Comparable<Business> {

	@ObjectId
	@Id
	private String _id;
	private String uuid;
	// Campo Obbligatorio
	private String name;
	// Campo obbligatorio
	private String pIva;
	// Campo obbligatorio
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
}
