package it.hash.osgi.user.attribute.persistence.mongo;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;

public class Activator extends DependencyActivatorBase{
	@Override
	public  void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent()
			.setInterface(AttributeServicePersistence.class.getName(), null)
			.setImplementation(AttributeServicePersistenceImpl.class)
			.add(createServiceDependency()
	                .setService(MongoDBService.class)
	                .setRequired(true))
		);
	}
   
	@Override
	public void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
