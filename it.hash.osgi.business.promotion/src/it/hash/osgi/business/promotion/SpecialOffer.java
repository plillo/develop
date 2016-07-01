package it.hash.osgi.business.promotion;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import it.hash.osgi.utils.StringUtils;

public class SpecialOffer extends Promotion {

	private String uuidProduct;
	private Double price;
	private Double minimumQuantity;
	private Double availability;
	// TODO
	// private Double stockRimanente;
	// Product
	private List<String> categories;
	private List<String> stores;
	private List<String> pictures;
	private String code;
	private String barcode;
	private String _locDescription;
	private String _locLongDescription;

	// TODO productPrice

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(Double minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	public Double getAvailability() {
		return availability;
	}

	public void setAvailability(Double availability) {
		this.availability = availability;
	}

	public String getUuidProduct() {
		return uuidProduct;
	}

	public void setUuidProduct(String uuidProduct) {
		this.uuidProduct = uuidProduct;
	}

	
	
	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getStores() {
		return stores;
	}

	public void setStores(List<String> stores) {
		this.stores = stores;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String get_locDescription() {
		return _locDescription;
	}

	public void set_locDescription(String _locDescription) {
		this._locDescription = _locDescription;
	}

	public String get_locLongDescription() {
		return _locLongDescription;
	}

	public void set_locLongDescription(String _locLongDescription) {
		this._locLongDescription = _locLongDescription;
	}

	@Override
	public void setByMap(Map<String, Object> map) {

		super.setByMap(map);
		if (map.containsKey("products")) {
			JSONObject obj = new JSONObject(map);
			if (obj != null) {
				JSONArray items = obj.optJSONArray("products");
				if (items != null) {
					JSONObject current = items.optJSONObject(0);
					this.code = current.optString("code");
					this.uuidProduct = current.optString("uuid");
					this.barcode = current.optString("barcode");
					this._locDescription = current.optString("_locDescription");
				}
			}
		}

		String attribute = null;
		Set<String> entry = map.keySet();
		for (Object elem : entry) {
			attribute = (String) elem;
			switch (attribute) {

			case "price":
				Object o=map.get(attribute);
				if (o instanceof Double )
				this.setPrice((Double) map.get(attribute));
				else 
					this.setPrice((Double.parseDouble((String)map.get(attribute))));
				break;
			case "quantity":
				this.setMinimumQuantity((Double) map.get(attribute));
				break;
			case "availability":
				if (map.get("availability") instanceof Double)
					this.setAvailability((Double) map.get(attribute));
				break;
			}
		}

	}

	@Override
	public String toString() {
		String str = super.toString();
		return str + "  SpecialOffer [uuidProduct=" + uuidProduct + ", price=" + price + ", minimumQuantity="
				+ minimumQuantity + ", availability=" + availability + ", categories=" + categories + ", stores="
				+ stores + ", pictures=" + pictures + ", code=" + code + ", barcode=" + barcode + ", _locDescription="
				+ _locDescription + ", _locLongDescription=" + _locLongDescription + "]";
	}

	@Override
	public Map<String, Object> toMap() {

		Map<String, Object> map = super.toMap();
	
		if (!StringUtils.isEON(this.getUuidProduct()))
			map.put("uuidProduct", this.getUuidProduct());
		if (this.getPrice()!=null)
			map.put("price", this.getPrice());
		if (this.getMinimumQuantity()!=null)
			map.put("minimumQuantity", this.getMinimumQuantity());
		if (this.getAvailability()!=null)
			map.put("availability", this.getAvailability());
		if (this.getCategories()!=null)
			map.put("categories", this.getCategories());
		if (this.getStores()!=null)
			map.put("stores", this.getStores());
		if (this.getPictures()!=null)
			map.put("pictures", this.getPictures());
		if (!StringUtils.isEON(this.getCode()))
			map.put("code", this.getCode());
		if (!StringUtils.isEON(this.getBarcode()))
			map.put("barcode", this.getBarcode());
		if (!StringUtils.isEON(this._locDescription))
			map.put("locDescription", this._locDescription);
		if (!StringUtils.isEON(this._locLongDescription))
			map.put("locLongDescription", this._locLongDescription);
			
		
		return map;
	}

}
