package it.hash.osgi.utils;

import java.util.Iterator;
import java.util.Map;

public class MapTools {
	public static boolean containsKeyAndNotNullValue(Map<String, Object> map, String key, String value){
		if(!map.containsKey(key))
			return false;

		return map.get(key) != null;
	}
	
	public static Map<String, Object> merge(Map<String, Object> to, Map<String, Object> from){
		for(Iterator<String> itrr = from.keySet().iterator();itrr.hasNext();){
			String key = itrr.next();
			to.put(key, from.get(key));
		}
		
		return to;
	}
}