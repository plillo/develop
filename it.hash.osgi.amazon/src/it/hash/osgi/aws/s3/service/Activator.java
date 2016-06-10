package it.hash.osgi.aws.s3.service;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;

import it.hash.osgi.aws.console.Console;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
		
		// AWS S3 service registration
		manager.add(createComponent()
			.setInterface(S3Service.class.getName(), null)
			.setImplementation(S3ServiceImpl.class)
			.add(createServiceDependency().setService(Console.class).setRequired(true))
		);
		
		// Shell commands registration
		properties.put(CommandProcessor.COMMAND_SCOPE, "aws-s3");
		properties.put(CommandProcessor.COMMAND_FUNCTION, new String[]{"create", "delete"});
		manager.add(createComponent()
			.setInterface(Object.class.getName(), properties)
			.setImplementation(ShellCommands.class)
			.add(createServiceDependency().setService(S3Service.class).setRequired(true)));
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}