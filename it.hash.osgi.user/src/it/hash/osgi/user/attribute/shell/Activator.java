package it.hash.osgi.user.attribute.shell;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;

import it.hash.osgi.user.attribute.service.AttributeService;

public class Activator extends DependencyActivatorBase{

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "uattr");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[] {"getAttributesByCategories","createAttribute"});
		manager.add(createComponent()
				.setInterface(Object.class.getName(), props)
				.setImplementation(Commands.class)
				.add(createServiceDependency().setService(AttributeService.class).setRequired(true)));	
	}   

	@Override
	public void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
	
}
