package it.hash.osgi.security.service;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

@Component(property={EventConstants.EVENT_TOPIC+"=*"})
public class EventHandlerImpl implements EventHandler {

	@Override
	public void handleEvent(Event event) {
		System.out.println("Event: " + event.getTopic());
	}
	
}
