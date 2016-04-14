package it.hash.osgi.resource.uuid.mongo;

import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import it.hash.osgi.resource.uuid.api.UUIDService;

public class Activator extends DependencyActivatorBase {
	@Override
	public  void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 200);
		
		manager.add(createComponent()
			.setInterface(UUIDService.class.getName(), props)
			.setImplementation(UUIDServiceImpl.class)
			.add(createServiceDependency()
	                .setService(MongoDBService.class)
	                .setRequired(true))
		);
	}
     
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
