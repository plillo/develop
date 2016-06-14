package it.hash.osgi.business.category.parser;

public interface ParserService {
	String getParserCode();
	boolean addCategoriesByUrl(String url);
}