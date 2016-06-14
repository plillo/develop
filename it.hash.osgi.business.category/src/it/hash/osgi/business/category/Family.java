package it.hash.osgi.business.category;

import java.util.ArrayList;
import java.util.List;
  
public class Family {
	String definition;
	String text;
	Integer code;
	List<Clazz> LstClasse;
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
	public List<Clazz> getLstClasse() {
		return LstClasse;
	}
	public void setLstClasse(List<Clazz> lstClasse) {
		LstClasse = lstClasse;
	}
	
	
	public Clazz getClasse(Integer code){
		for(Clazz clas: LstClasse){
			if (clas.code == code)
				return clas;
		}
		
		return null;
	}
	
	public boolean addClasse(Clazz clas){
		if (LstClasse== null)
			this.LstClasse=new ArrayList<Clazz>();
		if (!LstClasse.contains(clas))
			return LstClasse.add(clas);
		
		return false;
	}	
	
	public boolean removeClasse(Integer code){
		for(Clazz clas: LstClasse){
			if (clas.code == code)
				return this.LstClasse.remove(clas);
		}

		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LstClasse == null) ? 0 : LstClasse.hashCode());
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
		Family other = (Family) obj;
		if (LstClasse == null) {
			if (other.LstClasse != null)
				return false;
		} else if (!LstClasse.equals(other.LstClasse))
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

}
