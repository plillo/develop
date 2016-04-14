package it.hash.osgi.utils;

import java.util.StringTokenizer;
import java.util.Vector;

public class StringUtils {
	
	public static boolean isEmpty(String s) {
		if(s==null)
			return false;
		return "".equals(s);
	}

	// SHORT version
	public static boolean isE(String s) {
		return isNotEmptyOrNull(s);
	}
	
	public static boolean isEmptyOrNull(String s) {
		return s==null || "".equals(s);
	}
	
	// SHORT version
	public static boolean isEON(String s) {
		return isEmptyOrNull(s);
	}

	public static boolean isNotEmptyOrNull(String s) {
		return !isEmptyOrNull(s);
	}
	
	// SHORT version
	public static boolean isNotEON(String s) {
		return isNotEmptyOrNull(s);
	}
	
	public static String emptyIfNull(String s) {
		return s==null ? "" : s;
	}

	public static String nullIfEmpty(String s) {
		return s==null ? null : s.equals("") ? null : s;
	}

	public static String defaultIfNull(String s, String def_value) {
		return s==null ? def_value : s;
	}
	
	public static String defaultIfNullOrEmpty(String s, String value) {
		if (s == null || "".equals(s)) {
			return value;
		}
		return s;
	}

	public static String setIfNullOrEmpty(String s, String value) {
		return defaultIfNullOrEmpty(s, value);
	}

	public static String prefix(String s, String prefix) {
		if(s.startsWith(prefix))
			return s;
		else
			return prefix+s;
	}

	public static String suffix(String s, String suffix) {
		if(s.endsWith(suffix))
			return s;
		else
			return s+suffix;
	}

	public static String stripPrefix(String s, String prefix) {
		if(s.startsWith(prefix))
			return s.substring(prefix.length());
		else
			return s;
	}

	public static String stripSuffix(String s, String suffix) {
		if(s.endsWith(suffix))
			return s.substring(0,s.length()-suffix.length());
		else
			return s;
	}

	public static String stripPrefixAndSuffix(String s, String prefix, String suffix) {
		return stripSuffix(stripPrefix(s,prefix),suffix);
	}

	public static String stripPrefixAndSuffix(String s, String p) {
		return stripPrefixAndSuffix(s,p,p);
	}
	
	public static String[] splitAndTrim(String s) {
		return splitAndTrim(s, ",");
	}
	
	public static String[] splitAndTrim(String s, String sep) {
		if (s == null) {
			return null;
		}
		String[] arr = s.split(sep);
		for(int k=0; k<arr.length; k++)
			arr[k] = arr[k].trim();
		
		return arr;
	}
	
	public static String trim(final String str) {
		if (str == null) {
			return null;
		}
	
		return str.trim();
	}
	
	public static String trimWords(String s, int size) {
		return trimWords(s, size, "");
	}

	public static String trimWords(String s, int size, String suffix) {
		if(s==null || size<=0) 
			return "";

		if(size>=s.length()) 
			return s.trim();

		StringBuffer sb = (new StringBuffer( s.substring(0, size))).reverse();
		int pos = sb.indexOf(" ")>0?sb.indexOf(" "):0;

		return new StringBuffer(sb.substring(pos)).reverse().toString().trim()+suffix;
	}

	public static Vector<String> wrapWords(String s, int size) {
		Vector<String> lines = new Vector<String>();

		do {
			String line = trimWords(trim(s), size);
			if(line.equals(s)) {
				lines.add(line);
				break;
			}
			lines.add(line);
			s = s.substring(line.length()).trim();
		}
		while(s.length()>0);

		return lines;
	}

	public static boolean startsWithDigit(String str) {
		if(str==null || str.length()==0)
			return false;

		return Character.isDigit(str.charAt(0));
	}

	public static String replace(String text, String ... toReplace) {
		for (String s: toReplace) {
			String splitted[] = s.split("[,=]",2);
			text = text.replaceAll("\\["+splitted[0]+"\\]",splitted[1]);
		}

		return text;
	}

	public static String repeat(String text, int repetitions) {
		StringBuffer sb = new StringBuffer();

		for (int k = 0; k< repetitions; k++) {
			sb.append(text);
		}

		return sb.toString();
	}

	public static String capitalizeFirstLetters(String s) {
		return capitalizeFirstLetters(s, " ");
	}

	public static String capitalizeFirstLetters(String s, String delim) {
		if(s==null)
			return null;
		final StringTokenizer st = new StringTokenizer( s, delim, true );
		final StringBuilder sb = new StringBuilder();

		while ( st.hasMoreTokens() ) {
			String token = st.nextToken();
			token = String.format("%s%s", Character.toUpperCase(token.charAt(0)), token.substring(1).toLowerCase());
			sb.append( token );
		}

		return sb.toString();
	}

	public static String normalize(String s, String validChars) {
		return normalize(s, validChars, null, ' ');
	}

	public static String normalize(String s, String validChars, String oldChars, char newChar) {
		StringBuffer out = new StringBuffer();

		if(oldChars!=null)
			for(int k=0;k<oldChars.length(); k++) {
				s = s.replace(oldChars.charAt(k), newChar);
			}

		for(int k=0;k<s.length(); k++) {
			char c = s.charAt(k);
			if(validChars.indexOf(c)>=0)
				out.append(c);
		}

		return out.toString();
	}

	public static boolean setContains(String set, String type) {
		if(isEON(set) || isEON(type))
			return false;

		String[] ss = set.split(",");
		for(String e:ss)
			if(e.equals(type))
				return true;

		return false;
	}

	public static String wrap(String string, String left, String right) {
		return left+string+right;
	}


}