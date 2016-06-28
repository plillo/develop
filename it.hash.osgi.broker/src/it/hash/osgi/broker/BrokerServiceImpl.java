package it.hash.osgi.broker;

import java.util.Dictionary;

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(immediate=true, property={Constants.SERVICE_PID+"=it.hash.osgi.broker.service"})
public class BrokerServiceImpl implements BrokerService, ManagedService {
	@SuppressWarnings("rawtypes")
	private Dictionary properties;
	private CoreMqttClient mqttClient;
	
	@Activate
	void activate() {
		doLog("Broker service activation");
	}
	
	@Override
	public void subscribe(String topic, int qos) {
		if(mqttClient==null)
			doLog("MQTTClient not instantiated");
		else
			mqttClient.subscribe(topic, qos);
	}
	
	@Override
	public void subscribe(String topic){
		if(mqttClient==null)
			doLog("MQTTClient not instantiated");
		else
			mqttClient.subscribe(topic, 0);
	}
	
	@Override
	public void publish(String topic, int qos, String message) {
		if(mqttClient==null)
			doLog("MQTTClient not instantiated");
		else
			mqttClient.publish(topic, qos, message);
	}

	@Override
	public void publish(String topic, String message){
		if(mqttClient==null)
			doLog("MQTTClient not instantiated");
		else
			mqttClient.publish(topic, 0, message);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Dictionary getProperties(){
		return properties;
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		if(properties==null)
			return;
		
		doLog("Broker service configuration");
		
		this.properties = properties;
		
		if(mqttClient!=null)
			mqttClient.stopClient();
		
		mqttClient = new CoreMqttClient(this.properties);
		
		if(mqttClient==null)
			doLog("Error instantiating MQTTClient");
		else
			mqttClient.runClient();
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }

}
