package it.hash.osgi.user.password;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		manager.add(createComponent()
				.setInterface(Password.class.getName(), null)
				.setImplementation(PasswordImpl.class));
	}
	
	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {

	}
}
