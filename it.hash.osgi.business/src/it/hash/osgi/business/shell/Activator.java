package it.hash.osgi.business.shell;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;

import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.BusinessService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "business");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[] { "addBusiness", "deleteBusiness", "listBusiness",
				"updateBusiness", "getBusiness", "getByCodiceFiscale", "notFollowed"});
		manager.add(
				createComponent().setInterface(Object.class.getName(), props).setImplementation(businessCommands.class)
						.add(createServiceDependency().setService(BusinessService.class).setRequired(true))
						.add(createServiceDependency().setService(BusinessServicePersistence.class).setRequired(true)));

	}
   
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}

}
