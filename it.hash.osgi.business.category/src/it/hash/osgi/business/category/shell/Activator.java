package it.hash.osgi.business.category.shell;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;

import it.hash.osgi.business.category.parser.ParserManager;
import it.hash.osgi.business.category.service.CategoryService;

public class Activator extends DependencyActivatorBase {

	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE,"category");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[] {"addCategory", "deleteCategory", "retrieveCategories", "createAttribute","createCollectionBy"});
		manager.add(
				 createComponent()
				.setInterface(Object.class.getName(), props)
				.setImplementation(Commands.class)
				.add(createServiceDependency().setService(CategoryService.class).setRequired(true))
				.add(createServiceDependency().setService(ParserManager.class).setRequired(true))
				);
		
		System.out.println("Business category shell actived");
	}

	
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}

}
