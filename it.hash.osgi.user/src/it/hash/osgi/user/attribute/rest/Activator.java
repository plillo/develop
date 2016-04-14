package it.hash.osgi.user.attribute.rest;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.user.attribute.service.AttributeService;

public class Activator extends DependencyActivatorBase{

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Resources.class)
				.add(createServiceDependency().setService(AttributeService.class).setRequired(true)));

		doLog("User Attribute REST resources actived");
	}
	  
	private void doLog(String message) {
		System.out.println("## [" + this.getClass() + "] " + message);
	}

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {
	}
}
