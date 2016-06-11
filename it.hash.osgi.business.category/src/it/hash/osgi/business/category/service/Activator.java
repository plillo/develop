package it.hash.osgi.business.category.service;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.business.category.persistence.api.CategoryPersistence;
import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.user.attribute.service.AttributeService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(CategoryService.class.getName(),null)
				.setImplementation(CategoryServiceImpl.class)
				.add(createServiceDependency()
						.setService(CategoryPersistence.class)
						.setRequired(true))
				.add(createServiceDependency()
						.setService(UuidService.class)
						.setRequired(true))
				.add(createServiceDependency()
						.setService(AttributeService.class)
						.setRequired(true))
				);
	}
  
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
