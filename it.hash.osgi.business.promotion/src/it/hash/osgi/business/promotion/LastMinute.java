package it.hash.osgi.business.promotion;


import java.util.List;
import java.util.Map;
import java.util.Set;


import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.product.Product;

public class LastMinute extends Promotion {

	List<Product> products;
	List<Category> categories;
	Double discount;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Override
	public void setByMap(Map<String, Object> map) {
		super.setByMap(map);

		String attribute = null;
		Set<String> entry = map.keySet();
		for (Object elem : entry) {
			attribute = (String) elem;
			switch (attribute) {

			case "products":
				this.products = (List<Product>) (map.get(attribute));
				break;
				
			case "categories":
				this.categories = (List<Category>) map.get(attribute);
				break;
				
			case "discount":
				this.discount = (Double) map.get(attribute);
				break;
			}

		}
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();

		if (this.getProducts() != null)
			// map.put("products", this.getProducts());
			map.put("products", null);
		if (this.getCategories() != null)
			// map.put("categories", this.getCategories());
			map.put("categories", null);
		if (this.discount != null)
			map.put("discount", this.getDiscount());

		return map;
	}

	@Override
	public int compareTo(Promotion o) {
		if (this.getUuid().equals(o.getUuid()))
			return 0;
		return 1;
	}

}
