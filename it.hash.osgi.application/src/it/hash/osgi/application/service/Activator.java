package it.hash.osgi.application.service;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase{

	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
	}

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {

		manager.add(createComponent() 
				.setInterface(ApplicationManager.class.getName(), null) 
				.setImplementation(ApplicationManagerImpl.class)
				.add(createServiceDependency()
						.setService(ApplicationService.class)
						.setRequired(false)
						.setCallbacks("add", "remove")));
	}
}
