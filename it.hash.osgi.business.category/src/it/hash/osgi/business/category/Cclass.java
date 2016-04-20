package it.hash.osgi.business.category;

import java.util.ArrayList;
import java.util.List;
  
public class Cclass {
	String definition;
	String text;
	Integer code;
	List<Brick> LstBrick;
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
	public List<Brick> getLstBrick() {
		return LstBrick;
	}
	public void setLstBrick(List<Brick> lstBrick) {
		LstBrick = lstBrick;
	}

	public Brick getBrick(Integer code){
		for(Brick brick: LstBrick){
			if (brick.code == code)
				return brick;
		}
		
		return null;
	}		
	public boolean addBrick(Brick brick){
		if (LstBrick== null)
			this.LstBrick=new ArrayList<Brick>();
		if (!LstBrick.contains(brick))
			return LstBrick.add(brick);
		return false;
	}	
	
	public boolean removeBrick(Integer code){
		for(Brick brick: LstBrick){
			if (brick.code == code)
				return this.LstBrick.remove(brick);
		}
		
		
		return false;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LstBrick == null) ? 0 : LstBrick.hashCode());
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
		Cclass other = (Cclass) obj;
		if (LstBrick == null) {
			if (other.LstBrick != null)
				return false;
		} else if (!LstBrick.equals(other.LstBrick))
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

	
	

