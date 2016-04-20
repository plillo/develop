package it.hash.osgi.business.category;
  
import java.util.ArrayList;
import java.util.List;

public class Segment {
	String definition;
	String text;
	Integer code;
	List<Family> LstFamily;
	
	
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public List<Family> getLstFamily() {
		return LstFamily;
	}
	public void setLstFamily(List<Family> lstFamily) {
		LstFamily = lstFamily;
	}
	
	
	
	public Family getFamily(Integer code){
		for(Family family: LstFamily){
			if (family.code==code)
				return family;
		}
		
		return null;
	}		
	public boolean addFamily(Family family){
		if (LstFamily== null)
			this.LstFamily=new ArrayList<Family>();
		if (!LstFamily.contains(family))
			return LstFamily.add(family);
		return false;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LstFamily == null) ? 0 : LstFamily.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		Segment other = (Segment) obj;
		if (LstFamily == null) {
			if (other.LstFamily != null)
				return false;
		} else if (!LstFamily.equals(other.LstFamily))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	public boolean removeFamily(Integer code){
		for(Family family: LstFamily){
			if (family.code == code)
				return this.LstFamily.remove(family);
		}
		
		
		return false;
	}
	
	@Override
	public String toString(){
		String s=null;
		s=this.definition+" "+this.code+" "+this.text;
		
		return s;
		
	}
	
}
