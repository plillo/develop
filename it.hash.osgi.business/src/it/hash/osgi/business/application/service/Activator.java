package it.hash.osgi.business.application.service;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.application.service.ApplicationService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
  
		manager.add(createComponent()
				.setInterface(ApplicationService.class.getName(), properties)
				.setImplementation(ApplicationServiceImpl.class)
				);
		System.out.println("Business application service actived.");
	}

	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
