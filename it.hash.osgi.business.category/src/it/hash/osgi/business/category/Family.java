package it.hash.osgi.business.category;

import java.util.ArrayList;
import java.util.List;

public class Family {
	String definition;
	String text;
	Integer code;
	List<Cclass> LstClasse;
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
	public List<Cclass> getLstClasse() {
		return LstClasse;
	}
	public void setLstClasse(List<Cclass> lstClasse) {
		LstClasse = lstClasse;
	}
	
	
	public Cclass getClasse(Integer code){
		for(Cclass clas: LstClasse){
			if (clas.code == code)
				return clas;
		}
		
		return null;
	}		
	public boolean addClasse(Cclass clas){
		if (LstClasse== null)
			this.LstClasse=new ArrayList<Cclass>();
		if (!LstClasse.contains(clas))
			return LstClasse.add(clas);
		return false;
	}	
	
	public boolean removeClasse(Integer code){
		for(Cclass clas: LstClasse){
			if (clas.code == code)
				return this.LstClasse.remove(clas);
		}
		
		
		return false;
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
		Family other = (Family) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
	
}
