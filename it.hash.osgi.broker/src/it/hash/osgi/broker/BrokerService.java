package it.hash.osgi.broker;

import java.util.Dictionary;

public interface BrokerService {
	public void subscribe(String topic);
	public void subscribe(String topic, int qos);
	public void publish(String topic, String message);
	public void publish(String topic, int qos, String message);
	@SuppressWarnings("rawtypes")
	Dictionary getProperties();
}
