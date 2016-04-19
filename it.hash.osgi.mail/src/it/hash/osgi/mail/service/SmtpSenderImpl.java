package it.hash.osgi.mail.service;

import java.net.URL;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

import com.sun.mail.util.MailSSLSocketFactory;

import static it.hash.osgi.utils.Parser.*;
import static it.hash.osgi.utils.StringUtils.*;


@SuppressWarnings("rawtypes")
public class SmtpSenderImpl implements SmtpSender, ManagedService {
	Dictionary properties;
	
	static final String EMAIL_DEFAULT_RULE = "^((([a-z]|[0-9]|!|#|$|%|&|'|\\*|\\+|\\-|/|=|\\?|\\^|_|`|\\{|\\||\\}|~)+(\\.([a-z]|[0-9]|!|#|$|%|&|'|\\*|\\+|\\-|/|=|\\?|\\^|_|`|\\{|\\||\\}|~)+)*)@((((([a-z]|[0-9])([a-z]|[0-9]|\\-){0,61}([a-z]|[0-9])\\.))*([a-z]|[0-9])([a-z]|[0-9]|\\-){0,61}([a-z]|[0-9])\\.(af|ax|al|dz|as|ad|ao|ai|aq|ag|ar|am|aw|au|at|az|bs|bh|bd|bb|by|be|bz|bj|bm|bt|bo|ba|bw|bv|br|io|bn|bg|bf|bi|kh|cm|ca|cv|ky|cf|td|cl|cn|cx|cc|co|km|cg|cd|ck|cr|ci|hr|cu|cy|cz|dk|dj|dm|do|ec|eg|sv|gq|er|ee|et|eu|fk|fo|fj|fi|fr|gf|pf|tf|ga|gm|ge|de|gh|gi|gr|gl|gd|gp|gu|gt| gg|gn|gw|gy|ht|hm|va|hn|hk|hu|is|in|id|ir|iq|ie|im|il|it|jm|jp|je|jo|kz|ke|ki|kp|kr|kw|kg|la|lv|lb|ls|lr|ly|li|lt|lu|mo|mk|mg|mw|my|mv|ml|mt|mh|mq|mr|mu|yt|mx|fm|md|mc|mn|ms|ma|mz|mm|na|nr|np|nl|an|nc|nz|ni|ne|ng|nu|nf|mp|no|om|pk|pw|ps|pa|pg|py|pe|ph|pn|pl|pt|pr|qa|re|ro|ru|rw|sh|kn|lc|pm|vc|ws|sm|st|sa|sn|cs|sc|sl|sg|sk|si|sb|so|za|gs|es|lk|sd|sr|sj|sz|se|ch|sy|tw|tj|tz|th|tl|tg|tk|to|tt|tn|tr|tm|tc|tv|ug|ua|ae|gb|uk|us|um|uy|uz|vu|ve|vn|vg|vi|wf|eh|ye|zm|zw|com|edu|gov|int|mil|net|org|biz|info|name|pro|aero|coop|museum|arpa))|(((([0-9]){1,3}\\.){3}([0-9]){1,3}))|(\\[((([0-9]){1,3}\\.){3}([0-9]){1,3})\\])))$";
	Pattern emailPattern = Pattern.compile(EMAIL_DEFAULT_RULE);
	
	private volatile LogService logService;

	void start(){
		System.out.println("started service: "+this.getClass().getName());
	}
	
	@Override
	public void send(TreeMap<String, Object> pars) throws AddressException, MessagingException {
		boolean trace = true;
				
		String from = (String)pars.get("from");
		String recipient = (String)pars.get("recipient"); 
		String ccRecipient = (String)pars.get("ccrecipient");
		String bccRecipient = (String)pars.get("bccrecipient"); 
		String subject = (String)pars.get("subject"); 
		String messageText = (String)pars.get("messageText"); 
		String messageHtml = (String)pars.get("messageHtml"); 
		URL[] messageHtmlInline = (URL[])pars.get("messageHtmlInline"); 
		URL[] attachments = (URL[])pars.get("attachments");
		
		// Checks, if recipient is not the supervisor
		if(!recipient.equals((String)properties.get("supervisorEMail"))) {
			// Send enabled check
			if(!parseBoolean((String)properties.get("isSendMailEnabled"), false)) return;
	
			// Redirection check
			if(parseBoolean((String)properties.get("isSendMailRedirected"),false)) {
				// TRACE
				if(trace) {
					logService.log(LogService.LOG_INFO, "Setting redirection: ");
				}

				subject = "[redirect from: "+recipient+"] " + subject;
				String redirect = (String)properties.get("sendMailRedirectTo");
				if(isValidEMail(redirect)) 
					recipient = redirect;
				else {
					// TRACE
					if(trace) {
						logService.log(LogService.LOG_INFO, "Sending mail to supervisor: ");
					}

					sendMailToSupervisor(
							"Invalid redirection e-mail address", 
							"Invalid redirection e-mail address: "+redirect
							);
					return; 
				}
			}
		}

		try {
			// SET default sender if undefined
			if(isEON(from)) 
				from = (String)properties.get("sendMailFrom");
	
			// SET properties: get system props
			Properties props = System.getProperties();
	
			// GET configuration SMTP SERVER
			String smtp_server = (String)properties.get("smtpServer");
			if(isEON(smtp_server))
				smtp_server = "localhost";
	
			// GET configuration USERNAME and PASSWORD
			final String smtp_username = (String)properties.get("smtpUsername");
			final String smtp_password = (String)properties.get("smtpPassword");
	
			// SET default mail props
			props.put("mail.smtp.host", smtp_server);
			props.put("mail.mime.charset", "utf-8");
	
			// SET other configuration mail props
			for(Enumeration e = properties.keys(); e.hasMoreElements();) {
				String key = (String)e.nextElement();
				if(!key.startsWith("mail."))
					continue;
				props.put(key, (String)properties.get(key));
			}

			// If setted: TRUST ALL HOSTS
			if(parseBoolean((String)properties.get("isTrustAllHosts"), false)) {
				MailSSLSocketFactory sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
				props.put("mail.smtp.ssl.socketFactory", sf);
			}
	
			// TRACE
			if(trace) {
				logService.log(LogService.LOG_INFO, "Setted all properties: ");
			}

			// Get mail session with authenticator
			Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(smtp_username, smtp_password);
					}
			});

			// -- Create and build a new message --
			final MimeMessage msg = new MimeMessage(session);
	
			// TRACE
			if(trace) {
				logService.log(LogService.LOG_INFO, "Message, setting From");
			}
			// -- Set the FROM field --
			msg.setFrom(new InternetAddress(from));

			// TRACE
			if(trace) {
				logService.log(LogService.LOG_INFO, "Message, setting Recipient");
			}
			// -- Set the TO field --
			msg.addRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse(recipient, false)
					);

			// TRACE
			if(trace) {
				logService.log(LogService.LOG_INFO, "Message, setting CCrecipient");
			}
			// -- Set the CC recipient
			if (ccRecipient!=null && ccRecipient.length() > 0) {
				msg.addRecipients(
						Message.RecipientType.CC,
						InternetAddress.parse(ccRecipient, false)
						);
			}

			// TRACE
			if(trace) {
				logService.log(LogService.LOG_INFO, "Message, setting BCCrecipient");
			}
			// -- Set the CC recipient
			if (bccRecipient!=null && bccRecipient.length() > 0) {
				msg.addRecipients(
						Message.RecipientType.BCC,
						InternetAddress.parse(bccRecipient, false)
						);
			}

			// TRACE
			if(trace) {
				logService.log(LogService.LOG_INFO, "Message, setting Subject");
			}
			// -- Set the SUBJECT
			msg.setSubject(subject);

			// -- Set MIXED content
			EmailContentBuilder mailContentBuilder = new EmailContentBuilder();

			ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(javax.mail.Session.class.getClassLoader());
			try {
				// TRACE
				if(trace) {
					logService.log(LogService.LOG_INFO, "Message, building MIXED content");
				}
				
				final Multipart mpMixed = mailContentBuilder.build(
						messageText,
						messageHtml,
						messageHtmlInline,
						attachments);
				msg.setContent(mpMixed);
				msg.setSentDate(new Date());
				
				// TRACE
				if(trace) {
					logService.log(LogService.LOG_INFO, "Message, SENDING E-mail");
				}

				// SEND message
				Transport.send(msg); 
			}
			finally {
			  Thread.currentThread().setContextClassLoader(oldCl);
			}

			
			
			
			

	
			// TRACE
			if(trace) {
				logService.log(LogService.LOG_INFO, "E-mail sent to: "+recipient+", subject:"+subject);
			}
		}
		catch (SendFailedException e) {
			if(e.getValidSentAddresses()!=null)
				for(Address vsa:e.getValidUnsentAddresses()) {
					logService.log(LogService.LOG_ERROR, "Sent: "+vsa.toString());
				}
			if(e.getValidUnsentAddresses()!=null)
				for(Address vsa:e.getValidUnsentAddresses()) {
					logService.log(LogService.LOG_ERROR, "Not sent: "+vsa.toString());
				}
			if(e.getInvalidAddresses()!=null)
				for(Address ia:e.getInvalidAddresses()) {
					logService.log(LogService.LOG_ERROR, "Invalid address: "+ia.toString());
				}

			logService.log(LogService.LOG_ERROR, "Failure sending e-mail to: "+recipient+" "+e.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			logService.log(LogService.LOG_ERROR, "Failure sending e-mail to: "+recipient+" "+e.toString());
		}
	}
	
	private boolean isValidEMail(String input) {
		if(input==null || "".equals(input)) return false;

		return emailPattern.matcher(input.trim().toLowerCase()).find();
	}

	public void sendMailToSupervisor(String subject, String textbody) throws AddressException, MessagingException
	{
		TreeMap<String, Object> msg = new TreeMap<String, Object>();
		msg.put("from", properties.get("sendMailFrom"));
		msg.put("recipient", properties.get("supervisorEMail"));
		msg.put("subject", subject);
		msg.put("messageText", textbody);
		send(msg);
	}

	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		this.properties = properties;
	}

}
