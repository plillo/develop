package it.hash.osgi.resource.uuid.shell;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "uuid");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[] { "create", "delete", "list", "get" });
		manager.add(
				createComponent()
					.setInterface(Object.class.getName(), props)
					.setImplementation(uuidCommands.class)
					.add(createServiceDependency().setService(UUIDService.class).setRequired(true)));

	}

	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}

}
