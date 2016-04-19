package it.hash.osgi.business.category;

import java.util.ArrayList;
import java.util.List;
 
public class AttType {
	String definition;
	String text;
	Integer code;
	List<AttValue> attValue;
	
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
	public List<AttValue> getAttValue() {
		return attValue;
	}
	public void setAttValue(List<AttValue> attValue) {
		this.attValue = attValue;
	}
	
	public boolean addAttValue(AttValue attValue){
		if (this.attValue == null )
			this.attValue= new ArrayList<AttValue>();
		if (!this.attValue.contains(attValue))
			return this.attValue.add(attValue);
		
		return false;
		
	}

	public boolean removeAttValue(AttValue attValue){
	 
	return this.attValue.remove(attValue);
}
	
	public AttValue getAttValue(Integer code){
		for( AttValue aV :this.attValue){
			if (aV.getCode()== code)
				return aV;
		}	
		return null;
	
			
	
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		AttType other = (AttType) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
}
