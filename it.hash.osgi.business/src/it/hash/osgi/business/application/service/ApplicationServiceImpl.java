package it.hash.osgi.business.application.service;

import java.util.List;

import it.hash.osgi.application.service.ApplicationService;
import it.hash.osgi.user.attribute.Attribute;

public class ApplicationServiceImpl implements ApplicationService{

	@Override
	public String getCode() {
		return "bsnss-v1.0";
	}

	@Override
	public void filterAttributes(List<Attribute> attributes) {
		System.out.println("Business application service: filtering attributes...");
	}

}
