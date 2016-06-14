package it.hash.osgi.security.service;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		/*
		// Register REST resources
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Resources.class));
		
		// Register Security Handler
		manager.add(createComponent()
				.setInterface(new String[] {AuthenticationHandler.class.getName(),AuthorizationHandler.class.getName()}, null)
				.setImplementation(SecurityHandler.class)
				.add(createServiceDependency().setService(JWTService.class).setRequired(true)));
		
		// Register Security Service
		manager.add(createComponent()
				.setInterface(SecurityService.class.getName(),null)
				.setImplementation(SecurityServiceImpl.class));
		
		// Register JWT service
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.hash.osgi.security.jwt.service");
		manager.add(createComponent()
			.setInterface(new String[]{JWTService.class.getName(), ManagedService.class.getName()}, properties)
			.setImplementation(JWTServiceImpl.class)
			.add(createServiceDependency().setService(SecurityService.class).setRequired(true)));
		
		// Register "jwt" shell scope
    	Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "jwt");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[] {"token", "claims", "issuer"});
		manager.add(createComponent()
				.setInterface(Object.class.getName(), props)
				.setImplementation(Commands.class)
				.add(createServiceDependency().setService(JWTService.class).setRequired(true)));
		*/	
	}
	
	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
	}
}
