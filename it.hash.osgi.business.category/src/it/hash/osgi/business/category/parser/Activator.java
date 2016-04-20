package it.hash.osgi.business.category.parser;

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
				.setInterface(ParserManager.class.getName(), null) 
				.setImplementation(ParserManagerImpl.class)
				// Authenticators whiteboard
				.add(createServiceDependency()
						.setService(ParserService.class)
						.setCallbacks("add", "remove")));
	}
}
