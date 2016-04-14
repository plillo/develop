package it.hash.osgi.broker;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.security.jwt.service.JWTService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties serviceProps = new Properties();
		serviceProps.put(Constants.SERVICE_PID, "it.hash.osgi.broker.service");
		
		Properties shellProps = new Properties();
		shellProps.put(CommandProcessor.COMMAND_SCOPE, "broker");
		shellProps.put(CommandProcessor.COMMAND_FUNCTION, new String[] { "subscribe", "publish" });
		
		// Registering BrokerService
		manager.add(
				createComponent()
				.setInterface(new String[] { BrokerService.class.getName(), ManagedService.class.getName() }, serviceProps)
				.setImplementation(BrokerServiceImpl.class));
		
		// Registering REST resources
		manager.add(
				createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Resources.class)
				.add(createServiceDependency().setService(JWTService.class).setRequired(true)));
		
		// Registering shell commands
		manager.add(
				createComponent()
				.setInterface(Object.class.getName(), shellProps)
				.setImplementation(ShellCommands.class)
				.add(createServiceDependency().setService(BrokerService.class)));
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
