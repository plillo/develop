package it.hash.osgi.business.category;

import java.util.Map;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;
 
public class Category implements Comparable<Category>{
	 
	@ObjectId @Id
	private String _id;
	private String uuid;
	private String parentUuid;
	private Segment segment;
	private String name;
	private String code;
	private String _locDescription;
	private String _locLongDescription;
	private String cauthor;
	private String cdate;
	private String mauthor;
	private String mdate;
	private String lauthor;
	private String ldate;
	private Map <String,Object> extra;
	
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
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

	public Map <String,Object> getExtra() {
		return extra;
	}

	public void setExtra(Map <String,Object> extra) {
		this.extra = extra;
	}
	
	public void setExtra(String key, Object value){
		getExtra().put(key, value);
	}
	
	public Object getQualifiedExtra(String qualification, String key){
		return getExtra().get(key);
	}
	
	public void setQualifiedExtra(String qualification, String key, Object value) {
		setExtra(qualification + "." + key, value);
	}

	public static boolean isCode(String code) {
		String regex1= "[A-Z]{1}";
		String regex2= "[A-Z]{1}.[0-9]{2}";
		String regex3= "[A-Z]{1}.[0-9]{2}.[0-9]{2}";
		String regex4="[A-Z]{1}.[0-9]{2}.[0-9]{2}.[0-9]{2}";
//		String regex4= "[A-Z]|[A-Z].[0-9]{2}|[A-Z].[0-9]{2}.[0-9]{2}|[A-Z].[0-9]{2}.[0-9]{2}.[0-9]{2}";
		
   if (code.matches(regex1)||code.matches(regex2)||code.matches(regex3)||code.matches(regex4)){
	//if (code.matches(regex4))
		return true;}
		
			
			return false;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Category other = (Category) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(Category obj) {
		   return this.uuid.compareTo(obj.getUuid());
			
	
	}

	
}
