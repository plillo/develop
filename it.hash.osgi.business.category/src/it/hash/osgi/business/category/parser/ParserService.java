package it.hash.osgi.business.category.parser;


public interface ParserService {
	String getAppCode();
	boolean addCategoriesByUrl(String url);
}