package it.hash.osgi.business.category.persistence.mongo;

import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import it.hash.osgi.business.category.persistence.api.CategoryPersistence;

public class Activator extends DependencyActivatorBase {
    @Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 2);
    	manager.add(createComponent()
        	.setInterface(CategoryPersistence.class.getName(), props)
            .setImplementation(CategoryPersistenceImpl.class)
            .add(createServiceDependency()
                .setService(MongoDBService.class)
                .setRequired(true))
                );
    } 

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}
