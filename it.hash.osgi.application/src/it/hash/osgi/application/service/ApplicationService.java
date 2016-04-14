package it.hash.osgi.application.service;

import java.util.List;

import it.hash.osgi.user.attribute.Attribute;

public interface ApplicationService {
	String getCode();
	void filterAttributes(List<Attribute> attributes);
}