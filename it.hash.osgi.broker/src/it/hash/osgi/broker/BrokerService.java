package it.hash.osgi.broker;

public interface BrokerService {
	public void send();
	public void subscribe(String topic);
	public void publish(String topic, String message);
}
