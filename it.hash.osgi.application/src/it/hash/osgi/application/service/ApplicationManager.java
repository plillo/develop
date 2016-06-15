package it.hash.osgi.application.service;

import java.util.List;

import it.hash.osgi.user.attribute.Attribute;

public interface ApplicationManager {
	public void filterAttributes(String appCode, List<Attribute> attributes);
}
