package it.hash.osgi.runtest.restapi.rest;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	
	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent() 
				.setInterface(Object.class.getName(), null) 
				.setImplementation(Resources.class));
	}

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
