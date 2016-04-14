package it.hash.osgi.user.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

@XmlRootElement
public class Attribute  implements Comparable<Attribute>{
	@ObjectId @Id
	private String _id;
	private String uuid;
	private String name;
	private String label;
	private String type;
	private String UItype;
	private List<Map <String,Object>> values;
	private List<Map <String,Object>> applications;
	private String validator;
	private boolean mandatory;
	private boolean multiValued;
	private String cauthor;
	private String cdate;
	private String mauthor;
	private String mdate;
	private String lauthor;
	private String ldate;
	private Map <String,Object> others;
	   
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUItype() {
		return UItype;
	}
	public void setUItype(String uItype) {
		UItype = uItype;
	}
	public List<Map <String,Object>> getValues() {
		return values;
	}
	public void setValues(List<Map <String,Object>> values) {
		this.values = values;
	}
	public boolean addValues(Map<String,Object> value) {
		if (this.values==null)
			this.values= new ArrayList<Map<String,Object>>();
		if (!this.values.contains(value))
			return this.values.add(value);
		
		return false;
	}
	public boolean removeValues(String value){
		if (value!=null && this.values!=null)
			return this.values.remove(value);
		
		return false;
	}
	public List<Map<String,Object>> getApplications() {
		return applications;
	}
	public void setApplications(List<Map <String,Object>> applications) {
		this.applications = applications;
	}
	public boolean addApplications(Map<String,Object> application) {
		if (this.applications==null)
			this.applications= new ArrayList<Map<String,Object>>();
		if (!this.applications.contains(application))
			return this.applications.add(application);
		
		return false;
	}
	public boolean removeApplications(Map<String,Object> application){
		if (application!=null && this.applications!=null)
			return this.values.remove(application);
		
		return false;
	}
	public String getValidator() {
		return validator;
	}
	public void setValidator(String validator) {
		this.validator = validator;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	public boolean isMultiValued() {
		return multiValued;
	}
	public void setMultiValued(boolean multiValued) {
		this.multiValued = multiValued;
	}
	// EXTRA ATTRIBUTES
	// ================
	public Map<String, Object> getOthers() {
		return others;
	}
	public void setOthers(Map<String, Object> others) {
		this.others = others;
	}
	public Object setOthers(String attribute, Object o){
		return this.others.put(attribute, o);
	}
	public Object getOthers(String attribute){
		return this.others.get(attribute);
	}
	public Object removeOthers(String attribute){
		return this.others.remove(attribute);
	}
	
	// STANDARD ATTRIBUTES
	// ===================
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
		Attribute other = (Attribute) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Attribute obj) {
		 return this.uuid.compareTo(obj.getUuid());
	}
}
