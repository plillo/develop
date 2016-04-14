package it.hash.osgi.utils;

public class Parser {
	public static int parseInt(String value) {
		return Integer.parseInt(value);
	}

	public static int parseInt(String value, int default_value) {
		if(StringUtils.isEON(value))
			return default_value;
		try{
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return default_value;
		}
	}

	public static long parseLong(String value) {
		return Long.parseLong(value);
	}

	public static long parseLong(String value, long default_value) {
		if(StringUtils.isEON(value))
			return default_value;
		try{
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return default_value;
		}
	}
	
	public static float parseFloat(String value) {
		return Float.parseFloat(value);
	}

	public static float parseFloat(String value, float default_value) {
		if(StringUtils.isEON(value))
			return default_value;
		try{
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return default_value;
		}
	}

	public static double parseDouble(String value) {
		return Double.parseDouble(value);
	}

	public static double parseDouble(String value, double default_value) {
		if(StringUtils.isEON(value))
			return default_value;
		try{
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return default_value;
		}
	}

	public static boolean parseBoolean(String value) {
		return Boolean.parseBoolean(value);
	}

	public static boolean parseBoolean(String value, boolean default_value) {
		if(StringUtils.isEON(value))
			return default_value;
		try{
			return Boolean.parseBoolean(value);
		} catch (NumberFormatException e) {
			return default_value;
		}
	}


}