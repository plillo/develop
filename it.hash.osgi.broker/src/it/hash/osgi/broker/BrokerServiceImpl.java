package it.hash.osgi.broker;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Component;

@Component(immediate=true)
public class BrokerServiceImpl implements BrokerService, ManagedService {
	@SuppressWarnings("rawtypes")
	private Dictionary properties;
	private CoreMqttClient mqttClient;
	
	@Override
	public void send() {

	}
	
	@Override
	public void subscribe(String topic, int qos) {
		mqttClient.subscribe(topic, qos);
	}
	
	@Override
	public void subscribe(String topic){
		mqttClient.subscribe(topic, 0);
	}
	
	@Override
	public void publish(String topic, int qos, String message) {
		mqttClient.publish(topic, qos, message);
	}

	@Override
	public void publish(String topic, String message){
		mqttClient.publish(topic, 0, message);
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		if(properties==null)
			return;
		
		this.properties = properties;
		
		if(mqttClient!=null)
			mqttClient.stopClient();
		
		mqttClient = new CoreMqttClient(this.properties);
		mqttClient.runClient();
	}

}
