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
