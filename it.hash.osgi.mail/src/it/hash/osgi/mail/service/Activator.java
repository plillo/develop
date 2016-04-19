package it.hash.osgi.mail.service;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.hash.osgi.mail.service");
		
		manager.add(createComponent()
			.setInterface(new String[]{SmtpSender.class.getName(), ManagedService.class.getName()}, properties)
			.setImplementation(SmtpSenderImpl.class)
			.add(createServiceDependency().setService(LogService.class).setRequired(false)));
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
