package it.hash.osgi.qrcode;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	public void init(BundleContext context, DependencyManager dm) 
			throws Exception {
		dm.add(createComponent()
		.setInterface(Object.class.getName(), null)
		.setImplementation(QRCodeResources.class));
		
		System.out.println("QRCode REST resources actived");
	}

	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}
