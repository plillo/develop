package it.hash.osgi.broker;

public interface BrokerService {
	public void send();
	public void subscribe(String topic);
	public void subscribe(String topic, int qos);
	public void publish(String topic, String message);
	public void publish(String topic, int qos, String message);
}
