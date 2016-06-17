package it.hash.osgi.business.promotion;

import java.util.Map;

public class Three4two extends Promotion{

	@Override
	public void  setByMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> toMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(Promotion o) {
		if (this.getUuid().equals(o.getUuid()))
			return 0;
		return 1;
	}

}
