package it.hash.osgi.mail.shell;

import java.util.TreeMap;

import javax.mail.MessagingException;

import it.hash.osgi.mail.service.SmtpSender;

public class Commands {
	private volatile SmtpSender _mailService;

	void start(){
		System.out.println("started service: "+this.getClass().getName());
	}
	
	public void send(String recipient, String subject, String body) {
		System.out.println("sending to: "+recipient);
		
		TreeMap<String, Object> pars = new TreeMap<String, Object>();
		pars.put("from", "idalab@unisalento.it");
		pars.put("recipient", recipient);
		pars.put("subject", subject);
		pars.put("messageText", body);
		
		try {
			_mailService.send(pars);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void test() {
		System.out.println("test");
	}

}