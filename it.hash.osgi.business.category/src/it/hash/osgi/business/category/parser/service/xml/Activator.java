package it.hash.osgi.business.category.parser.service.xml;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.business.category.parser.ParserService;
import it.hash.osgi.business.category.service.CategoryService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
  
		manager.add(createComponent()
				.setInterface(ParserService.class.getName(), properties)
				.setImplementation(ParserServiceImpl.class)
				 .add(createServiceDependency()
			                .setService(CategoryService.class)
			                .setRequired(true))
				);
		System.out.println("Parser category xml service actived.");
	}

	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
