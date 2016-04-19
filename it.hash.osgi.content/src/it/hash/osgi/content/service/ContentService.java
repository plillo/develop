package it.hash.osgi.content.service;

import java.util.Map;

public interface ContentService {
	Map<String, Object> getContent(String name, String lang, String type);
	Map<String, Object> getContent(String name, String lang);
	Map<String, Object> getContent(String name);
	Map<String, Object> setContent(String name, String lang, String type, byte[] content);
}
