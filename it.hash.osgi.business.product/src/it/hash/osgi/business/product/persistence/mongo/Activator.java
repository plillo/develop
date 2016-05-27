package it.hash.osgi.business.product.persistence.mongo;

import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.log.LogService;

import it.hash.osgi.business.product.persistence.api.ProductPersistence;

public class Activator extends DependencyActivatorBase {
    @Override
    
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 200);
    	
		manager.add(createComponent()
        	.setInterface(ProductPersistence.class.getName(), props)
            .setImplementation(ProductPersistenceImpl.class)
            .add(createServiceDependency()
                .setService(LogService.class)
                .setRequired(false))
            .add(createServiceDependency()
                .setService(MongoDBService.class)
                .setRequired(true))
        );
		
		System.out.println("Product persistence: Mongo implementation actived");
    }

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}
