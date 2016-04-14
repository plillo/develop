package it.hash.osgi.aws.console;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		// Console registration
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.hash.osgi.aws.console");
		manager.add(createComponent()
			.setInterface(new String[]{Console.class.getName(), ManagedService.class.getName()}, properties)
			.setImplementation(ConsoleImpl.class)
			.add(createServiceDependency().setService(LogService.class).setRequired(false)));
		
		// Shell Commands registration
		properties = new Properties();
		properties.put(CommandProcessor.COMMAND_SCOPE, "aws");
		properties.put(CommandProcessor.COMMAND_FUNCTION, new String[]{"ec2", "s3", "sdb", "ses", "sqs", "dyndb", "createtable", "putuser", "listusers"});
		manager.add(createComponent()
			.setInterface(Object.class.getName(), properties)
			.setImplementation(ShellCommands.class)
			.add(createServiceDependency().setService(Console.class).setRequired(true)));
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
