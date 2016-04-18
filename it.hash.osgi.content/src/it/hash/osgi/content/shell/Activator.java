package it.hash.osgi.content.shell;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;

import it.hash.osgi.content.service.ContentService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "content");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[]{"create"});
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), props)
				.setImplementation(Commands.class)
				.add(createServiceDependency()
						.setService(ContentService.class)
						.setRequired(true)));
	
			System.out.println("Content SHELL resources actived");
		}

		@Override
		public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
		}

}
