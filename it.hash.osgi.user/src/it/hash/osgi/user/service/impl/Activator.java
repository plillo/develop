package it.hash.osgi.user.service.impl;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.application.service.ApplicationManager;
import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.security.jwt.service.JWTService;
import it.hash.osgi.user.service.api.UserService;
import it.hash.osgi.user.attribute.service.AttributeService;
import it.hash.osgi.user.password.Password;
import it.hash.osgi.user.persistence.api.UserPersistenceService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.hash.osgi.user.service");
		
		manager.add(createComponent()
			.setInterface(new String[]{UserService.class.getName(), ManagedService.class.getName()}, properties)
			.setImplementation(UserServiceImpl.class)
			.add(createServiceDependency().setService(UserPersistenceService.class).setRequired(true))
			.add(createServiceDependency().setService(EventAdmin.class).setRequired(true))
			.add(createServiceDependency().setService(JWTService.class).setRequired(true))
		    .add(createServiceDependency().setService(Password.class).setRequired(true))
            .add(createServiceDependency().setService(UuidService.class).setRequired(true))
            .add(createServiceDependency().setService(AttributeService.class).setRequired(true))
            .add(createServiceDependency().setService(ApplicationManager.class).setRequired(true))
		);
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
