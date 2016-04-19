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
		Cclass other = (Cclass) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}

	
	

