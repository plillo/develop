package it.hash.osgi.user.attribute.service;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;

public class Activator extends DependencyActivatorBase{

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent()
			.setInterface(AttributeService.class.getName(), null)
			.setImplementation(AttributeServiceImpl.class)
			.add(createServiceDependency()
					.setService(AttributeServicePersistence.class)
					.setRequired(true))
			.add(createServiceDependency()
					.setService(UUIDService.class)
					.setRequired(true))
	
		);
	}
   
	@Override
	public void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}

}
