package it.hash.osgi.runuuid;


import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	public void init(BundleContext context, DependencyManager dm) 
			throws Exception {
	
		System.out.println("=======\nRUN-UUID\n=======");
	}

	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}
