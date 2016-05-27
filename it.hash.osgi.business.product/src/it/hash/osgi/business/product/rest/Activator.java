package it.hash.osgi.business.product.rest;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.business.product.service.ProductService;

public class Activator extends DependencyActivatorBase {
    @Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Resources.class)
				.add(createServiceDependency().setService(ProductService.class).setRequired(true)));

		System.out.println("Product REST resources actived");
    }

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}
