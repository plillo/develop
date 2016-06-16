package it.hash.osgi.broker;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate=true, 
	service = Commands.class, 
	property = {
		CommandProcessor.COMMAND_SCOPE+"=broker",
		CommandProcessor.COMMAND_FUNCTION+"=publish",
		CommandProcessor.COMMAND_FUNCTION+"=subscribe"
	}
)
public class Commands {
	// References
	private BrokerService _brokerService;
	
	@Reference(service=BrokerService.class)
	public void setBrokerService(BrokerService service){
		_brokerService = service;
		doLog("JWTService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetBrokerService(BrokerService service){
		doLog("JWTService: "+(service==null?"NULL":"released"));
		_brokerService = null;
	}
	// === end references
	
	public void subscribe(String topic) {
		_brokerService.subscribe(topic);
		
		System.out.println("subscribed topic: " + topic);
	}
	
	public void publish(String topic, String message) {
		_brokerService.publish(topic, message);

		System.out.println("published topic: " + topic+ " message: "+message);
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}

