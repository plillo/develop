package it.hash.osgi.business.category.rest;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.business.category.service.CategoryService;


public class Activator extends DependencyActivatorBase {
    @Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Resources.class)
				.add(createServiceDependency().setService(CategoryService.class).setRequired(true)));

		System.out.println("Business category REST resources actived");
    }

      
    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}
