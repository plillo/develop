package it.hash.osgi.business.product.service;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.business.product.persistence.api.ProductPersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;


public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
  
		manager.add(createComponent()
			.setInterface( ProductService.class.getName() ,
					properties)
			.setImplementation(ProductServiceImpl.class)
			.add(createServiceDependency().setService(ProductPersistence.class).setRequired(true))
			.add(createServiceDependency().setService(CategoryService.class).setRequired(true))
			.add(createServiceDependency().setService(EventAdmin.class).setRequired(true))
			.add(createServiceDependency().setService(UUIDService.class).setRequired(true))
		);
		System.out.println("Product service actived.");
	}

	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
