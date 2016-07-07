package it.hash.osgi.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.util.JSON;

import it.hash.osgi.geojson.Point;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

@XmlRootElement
public class User implements Comparable<User> {
	@ObjectId @Id
	private String _id;
	private String uuid;
	private String username;
	private String password;
	private String salted_hash_password;
	private String firstName;
	private String lastName;
	private String password_mdate;
	private String email;
	private String mobile;
	private List<String> groups;
	private List<AttributeValue> attributes;
	private Point position;
	private Double radius;

	private Map<String, Object> extra;
	private String published;
	private String last_login_date;
	private String last_login_ip;
	private String trusted_email;
	private String trusted_mobile;
	private String cauthor;
	private String cdate;
	private String mauthor;
	private String mdate;
	private String lauthor;
	private String ldate;
	private String user_data;

	public String get_id() {
		return _id;
	}

	public void set_id(String id) {
		this._id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalted_hash_password() {
		return salted_hash_password;
	}

	public void setSalted_hash_password(String salted_hash_password) {
		this.salted_hash_password = salted_hash_password;
	}

	public String getPassword_mdate() {
		return password_mdate;
	}

	public void setPassword_mdate(String password_mdate) {
		this.password_mdate = password_mdate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	
	public void addGroup(String group) {
		if(this.groups==null)
			this.groups = new Vector<String>();
		
		this.groups.add(group);
	}
	
	public void removeGroup(String group) {
		if(this.groups==null)
			return;
		
		this.groups.remove(group);
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getLast_login_date() {
		return last_login_date;
	}

	public void setLast_login_date(String last_login_date) {
		this.last_login_date = last_login_date;
	}

	public String getLast_login_ip() {
		return last_login_ip;
	}

	public void setLast_login_ip(String last_login_ip) {
		this.last_login_ip = last_login_ip;
	}

	public String getTrusted_email() {
		return trusted_email;
	}

	public void setTrusted_email(String trusted_email) {
		this.trusted_email = trusted_email;
	}

	public String getTrusted_mobile() {
		return trusted_mobile;
	}

	public void setTrusted_mobile(String trusted_mobile) {
		this.trusted_mobile = trusted_mobile;
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

	public String getUser_data() {
		return user_data;
	}

	public void setUser_data(String user_data) {
		this.user_data = user_data;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public Object getExtra(String key) {
		return getExtra().get(key);
	}

	public void setExtra(String key, Object value) {
		if (getExtra() == null) {
			Map<String, Object> extra = new HashMap<String, Object>();
			this.setExtra(extra);
		}
		getExtra().put(key, value);
	}

	public Object getQualifiedExtra(String qualification, String key) {
		return getExtra().get(key);
	}

	public void setQualifiedExtra(String qualification, String key, Object value) {
		setExtra(qualification + "." + key, value);
	}

	public void removeExtra(String key) {
		this.extra.remove(key);
	}

	public List<AttributeValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeValue> attributes) {
		if (this.attributes==null)
			this.attributes = attributes;
		else 
		{
			for (AttributeValue elem:attributes){
				if (this.attributes.contains(elem))
					this.attributes.remove(elem);
				this.attributes.add(elem);
			}
		}
	}

	public boolean addAttribute(AttributeValue attribute) {
		if (this.getAttributes() == null)
			attributes = new ArrayList<AttributeValue>();
		if (this.attributes.contains(attribute))
			this.attributes.remove(attribute);
		
		return this.attributes.add(attribute);
	}

	public boolean removeAttribute(AttributeValue attribute) {
		if (this.attributes.contains(attribute))
			return this.attributes.remove(attribute);
		else
			return false;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		User other = (User) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	// implementing Comparable
	@Override
	public int compareTo(User obj) {
		if(this.uuid==null || obj==null || obj.uuid==null)
			return -1;
		return this.uuid.compareTo(obj.uuid);
	}
	
	public static Map<String, Object> toMap(User entity){
		Map<String, Object> map = new TreeMap<String, Object>();
		if(entity==null)
			return null;

		if(entity.getUuid()!=null)
			map.put("uuid",entity.getUuid());
		if(entity.getUsername()!=null)
			map.put("username",entity.getUsername());
		if(entity.getFirstName()!=null)
			map.put("firstName",entity.getFirstName());
		if(entity.getLastName()!=null)
			map.put("lastName",entity.getLastName());
		if(entity.getEmail()!=null)
			map.put("email",entity.getEmail());
		if(entity.getMobile()!=null)
			map.put("mobile",entity.getMobile());
		if(entity.getPassword()!=null)
			map.put("password",entity.getPassword());
		if(entity.getSalted_hash_password()!=null)
			map.put("salted_hash_password",entity.getSalted_hash_password());
		
		if(entity.getAttributes()!=null) {
			ArrayList<Map<String, Object>> dbl = new ArrayList<Map<String, Object>>();
			// Insert user's attributes into ArrayList
			for(AttributeValue attr: entity.getAttributes()){
				Map<String, Object> obj = new TreeMap<String, Object>();
				String uuid = attr.getAttributeUuid();
				Map<String, Object> jso = attr.getValue();
				try {
					String json = new ObjectMapper().writeValueAsString(jso);
					obj.put("attributeUuid", uuid);
					obj.put("value", JSON.parse(json));
					dbl.add(obj);
				} catch (Exception e) {
				}
			}
			// Put ArrayList
			map.put("attributes", dbl);
		}
		if (entity.getPosition()!=null)
			map.put("position", entity.getPosition().toMap());
		if (entity.getRadius()!=null)
			map.put("radius", entity.getRadius());

		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static User toUser(Map<String, Object> mapUser) {
		User user = new User();
		
		Set<String> entry = mapUser.keySet();
		for (String elem : entry) {
			switch (elem) {
			case "_id":
				user.set_id(mapUser.get(elem).toString());
				break;
			case "uuid":
				user.setUuid((String) mapUser.get(elem));
				break;
			case "username":
				user.setUsername((String) mapUser.get(elem));
				break;
			case "password":
				user.setPassword((String) mapUser.get(elem));
				break;
			case "salted_hash_password":
				user.setSalted_hash_password((String) mapUser.get(elem));
				break;
			case "firstName":
				user.setFirstName((String) mapUser.get(elem));
				break;
			case "lastName":
				user.setLastName((String) mapUser.get(elem));
				break;
			case "password_mdate":
				user.setPassword_mdate((String) mapUser.get(elem));
				break;
			case "email":
				user.setEmail((String) mapUser.get(elem));
				break;
			case "mobile":
				user.setMobile((String) mapUser.get(elem));
				break;
			case "groups":
				user.setGroups((List<String>) mapUser.get(elem));
				break;
			case "attributes":
				user.setAttributes((List<AttributeValue>) mapUser.get(elem));
				break;
			case "position":
				if(mapUser.get("position") instanceof Point)
					user.setPosition((Point) mapUser.get("position"));
				else if(mapUser.get("position") instanceof Map){
					Map<String, Object> position = (Map<String, Object>)mapUser.get("position");
					Map<String, Object> coordinates = (Map<String, Object>)position.get("coordinates");
					user.setPosition(new Point((double)coordinates.get("lat"), (double)coordinates.get("lng")));
				}
				break;
			case "radius":
				user.setRadius((double) mapUser.get(elem));
				break;
			case "published":
				user.setPublished((String) mapUser.get(elem));
				break;
			case "last_login_date":
				user.setLast_login_date((String) mapUser.get(elem));
				break;
			case "last_login_ip":
				user.setLast_login_ip((String) mapUser.get(elem));
				break;
			case "trusted_email":
				user.setTrusted_email((String) mapUser.get(elem));
				break;
			case "trusted_mobile":
				user.setTrusted_mobile((String) mapUser.get(elem));
				break;
			case "cauthor":
				user.setCauthor((String) mapUser.get(elem));
				break;
			case "cdate":
				user.setCdate((String) mapUser.get(elem));
				break;
			case "mauthor":
				user.setMauthor((String) mapUser.get(elem));
				break;
			case "mdate":
				user.setMdate((String) mapUser.get(elem));
				break;
			case "lauthor":
				user.setLauthor((String) mapUser.get(elem));
				break;
			case "ldate":
				user.setLdate((String) mapUser.get(elem));
				break;
			case "user_data":
				user.setUser_data((String) mapUser.get(elem));
				break;

			default:
				if (user.getExtra() == null)
					user.setExtra(new HashMap<String, Object>());
				if (!user.getExtra().containsKey(elem))
					user.setExtra(elem, mapUser.get(elem));

			}

		}
		return user;
	}


}
