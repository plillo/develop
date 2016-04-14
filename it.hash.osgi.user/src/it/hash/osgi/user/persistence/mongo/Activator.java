package it.hash.osgi.user.persistence.mongo;

import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.log.LogService;

import it.hash.osgi.user.password.Password;
import it.hash.osgi.user.persistence.api.UserServicePersistence;

public class Activator extends DependencyActivatorBase {
    @Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 200);
    	manager.add(createComponent()
        	.setInterface(UserServicePersistence.class.getName(), props)
            .setImplementation(UserServicePersistenceImpl.class)
            .add(createServiceDependency()
                    .setService(LogService.class)
                    .setRequired(false))
            .add(createServiceDependency()
                    .setService(Password.class)
                    .setRequired(true))
            .add(createServiceDependency()
                .setService(MongoDBService.class)
                .setRequired(true))
        );
    }   

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}
