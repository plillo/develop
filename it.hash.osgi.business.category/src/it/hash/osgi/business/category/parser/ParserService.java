package it.hash.osgi.business.category.parser;


public interface ParserService {
	String getAppCode();
	boolean createCollectionBy(String url, String nomefile);
}