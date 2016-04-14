package it.hash.osgi.utils;

public class Random {
	public static String getRandomKey(int len){
		return getRandomKey(len, null, 0)[0];
	}

	public static String[] getRandomKey(int len, String chars){
		return getRandomKey(len, null, 0, chars);
	}

	public static String[] getRandomKey(int len, String sep, int jmp){
		return getRandomKey(len, sep, jmp, "abcdefghijklmnpqrstuvwxyz123456789ABCDEFGHIJKLMNPQRSTUVWXYZ");
	}

	public static String[] getRandomKey(int len, String sep, int jmp, String chars){
		// Default: no format
		StringBuffer def = new StringBuffer();
		// Serial number format
		StringBuffer snf = new StringBuffer();

		int max = chars.length();

		boolean isSep = sep != null && !"".equals(sep);
		boolean isJmp = jmp > 0;

		for(int i=0; i<len; i++){
			if(isSep && isJmp && i > 0 && i % jmp == 0) snf.append(sep);
			char c = chars.charAt(new Double(Math.random()*max).intValue());
			def.append(c);
			snf.append(c);
		}

		return new String[] {def.toString(), snf.toString()};
	}
}
