package it.hash.osgi.business.promotion;

import java.util.HashMap;
import java.util.Map;

public class SpecialPrice extends Promotion {
	
	private String uuidProduct;
	// TODO productPrice
	
	public String getUuidProduct() {
		return uuidProduct;
	}

	public void setUuidProduct(String uuidProduct) {
		this.uuidProduct = uuidProduct;
	}

	@Override
	public void setByMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
	//	this.set_id((String) map.get("_id"));
		this.setUuid((String) map.get("uuid"));
		this.setType((String) map.get("type"));
		this.setBusinessName((String) map.get("businessName"));
		
	}

	@Override
	public Map<String, Object> toMap() {
		// TODO Auto-generated method stub
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("uuid", this.getUuid());
		map.put("type", this.getType());
		map.put("businessName", this.getBusinessName());
		return map;
	}

	@Override
	public int compareTo(Promotion p) {
		// TODO Auto-generated method stub
		if (this.getUuid().equals(p.getUuid()))
			return 0;
		return 1;
	}
	

}
