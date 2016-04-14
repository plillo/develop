package it.hash.osgi.business.persistence.mongo;

import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.log.LogService;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;

      
public class Activator extends DependencyActivatorBase {
    @Override
    
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 200);
    	
		manager.add(createComponent()
        	.setInterface(BusinessServicePersistence.class.getName(), props)
            .setImplementation(BusinessServicePersistenceImpl.class)
            .add(createServiceDependency()
                    .setService(LogService.class)
                    .setRequired(false))
            .add(createServiceDependency()
                .setService(MongoDBService.class)
                .setRequired(true))
                
                         );
		
		System.out.println("Business Persistence Mongo Actived");
    }

    
    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}
