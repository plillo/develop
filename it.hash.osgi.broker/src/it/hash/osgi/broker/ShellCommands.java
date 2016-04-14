package it.hash.osgi.broker;

public class ShellCommands {
	private volatile BrokerService _brokerService;
	
	public void subscribe(String topic) {
		_brokerService.subscribe(topic);
		
		System.out.println("subscribed topic: " + topic);
	}
	
	public void publish(String topic, String message) {
		_brokerService.publish(topic, message);

		System.out.println("published topic: " + topic+ " message: "+message);
	}
}

