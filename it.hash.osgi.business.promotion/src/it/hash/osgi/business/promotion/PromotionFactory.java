package it.hash.osgi.business.promotion;

import java.util.Map;

public class PromotionFactory {

	public static Promotion  getInstance(Map<String, Object> map) {

		String attribute = null;
		// Map<String, Object> others = new TreeMap<String, Object>();

		if (map.containsKey("type")) {
			attribute = (String) map.get("type");
			
			switch (attribute) {
			
			case "SPO":
				return new SpecialOffer();

			case "LastMinute":
				return new LastMinute();
		
			case "Three4two":
				return new Three4two();

			}

		}

		return null;

	}
	public static Promotion  getInstance(String type) {

			
			switch (type) {
			
			case "SPO":
				return new SpecialOffer();

			case "LastMinute":
				return new LastMinute();
		
			case "Three4two":
				return new Three4two();

			}

		
		return null;

	}
}
