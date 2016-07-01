package it.hash.osgi.business.promotion;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.product.Product;

public class LastMinute extends Promotion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	@SuppressWarnings("unchecked")
	@Override
	public void setByMap(Map<String, Object> map) {

		super.setByMap(map);

		String attribute = null;
		Object tmpO;
		Set<String> entry = map.keySet();
		for (Object elem : entry) {
			attribute = (String) elem;
			switch (attribute) {

			case "products":

				if (map.get(attribute) instanceof List) {
					List<Product> list = (List<Product>) (map.get(attribute));
					this.products = list;
				} else
					tmpO = map.get(attribute);
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
			map.put("products", this.getProducts());

		if (this.getCategories() != null)
			map.put("categories", this.getCategories());

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
