package it.hash.osgi.broker;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import it.hash.osgi.utils.Parser;
import static it.hash.osgi.utils.StringUtils.*;

public class CoreMqttClient implements MqttCallback {
	MqttClient myClient = null;
	MqttConnectOptions connOpt;

	// Broker parameters
	boolean cleanSession;
	String brokerUrl;
	String brokerPort;
	int keepAliveInterval;
	String username;
	String password;

	public CoreMqttClient(@SuppressWarnings("rawtypes") Dictionary properties) {
		cleanSession = Parser.parseBoolean((String)properties.get("clean-session"), true);
		keepAliveInterval = Parser.parseInt((String)properties.get("keep-alive-interval"), 30);
		brokerPort = defaultIfNullOrEmpty((String)properties.get("broker-port"),"1883");
		username = defaultIfNullOrEmpty((String)properties.get("username"),"admin");
		password = defaultIfNullOrEmpty((String)properties.get("password"),"admin");
		brokerUrl = defaultIfNullOrEmpty((String)properties.get("broker-url"),"tcp://localhost");
	}

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("-------------------------------------------------");
		System.out.println("| Broker connection lost");
		System.out.println("-------------------------------------------------");
		
		/*
		do {
			try {
				SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			try {
				logger.info("Trying to reconnect");
				listenToMqtt();
			} catch (Exception e) {
				logger.warn("Reconnect failed", e);
			}
		} while (!MqttClient.this.client.isConnected());
		logger.info("Successfully reconnected");
		*/
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + new String(message.getPayload()));
		System.out.println("-------------------------------------------------");
	}
	
	public void runClient() {
		if(myClient==null){
			// setup MQTT Client
			String clientID = MqttClient.generateClientId();
			connOpt = new MqttConnectOptions();

			connOpt.setCleanSession(cleanSession);
			connOpt.setKeepAliveInterval(keepAliveInterval);
			connOpt.setUserName(username);
			connOpt.setPassword(password.toCharArray());
			
			String uri = brokerUrl + ":" + brokerPort;

			System.out.println("Connecting to broker at: " + uri);
			
			// Connect to Broker
			try {
				myClient = new MqttClient(uri, clientID);
				myClient.setCallback(this);
				myClient.connect(connOpt);
				
				System.out.println("Connected to broker at: " + uri);
			} catch (MqttException e) {
				System.out.println(e.toString());
			}
		}
	}
	
	public void stopClient(){
		if(myClient!=null)
			try {
				myClient.disconnect();
				myClient = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void subscribe(String topic, int subQoS) {
		try {
			myClient.subscribe(topic, subQoS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void publish(String topic, int pubQoS, String message) {
		MqttTopic myTopic = myClient.getTopic(topic);
		
		byte[] bytes;
		try {
			bytes = URLEncoder.encode(message, "UTF-8").getBytes();
			message = URLDecoder.decode(new String(bytes, StandardCharsets.UTF_8), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			bytes = new byte[]{}; // void bytes array
		}
		MqttMessage msg = new MqttMessage(bytes);
		
		msg.setQos(pubQoS);
		msg.setRetained(false);

    	// Publish the message
    	System.out.println("Publishing to topic \"" + topic + "\" msg " + message + " qos " + pubQoS);
    	MqttDeliveryToken token = null;
    	try {
    		// publish message to broker
			token = myTopic.publish(msg);
			token.waitForCompletion();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
