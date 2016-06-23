package it.hash.osgi.business.promotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import it.hash.osgi.business.product.Product;

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
				this.setPrice(Double.valueOf((String) map.get(attribute)));
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
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uuid", this.getUuid());
		map.put("type", this.getType());
		map.put("businessName", this.getBusinessName());
		return map;
	}

}
