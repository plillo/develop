package it.hash.osgi.business.category;

import java.util.ArrayList;
import java.util.List;

public class Brick {
	String definition;
	String text;
	Integer code;
	List<AttType> LstAttType;
	
	public Brick( String definition, String text, Integer code ){
		this.definition=definition;
		this.text=text;
		this.code=code;
	}

	public Brick(){}
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

	public List<AttType> getListAttType() {
		return LstAttType;
	}

	public void setListAttType(List<AttType> LstAttType) {
		this.LstAttType = LstAttType;
	}

	
	public boolean addAttType(AttType attType){
		if (this.LstAttType==null)
			this.LstAttType= new ArrayList<AttType>();
		if (!this.LstAttType.contains(attType))
			return this.LstAttType.add(attType);
		return false;
		
	}
	public boolean removeAttType(Integer code){
		for (AttType aT: this.LstAttType){
			if (aT.code==code)
				return this.LstAttType.remove(aT);
		}
	
		return false ;
	}
	public AttType getAttType(Integer codeAttType){
		for (AttType aT: this.LstAttType){
			if (aT.code==codeAttType)
				return aT;
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
		Brick other = (Brick) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	
	
}
