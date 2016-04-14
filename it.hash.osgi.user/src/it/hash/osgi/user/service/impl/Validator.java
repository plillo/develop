package it.hash.osgi.user.service.impl;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class Validator {
	static final String USERNAME_DEFAULT_RULE = "^([a-z]|[0-9]|\\.){4,32}$";
	static final String PASSWORD_DEFAULT_RULE = "^([a-z]|[A-Z]|[0-9]){4,16}$";
	static final String EMAIL_DEFAULT_RULE = "^((([a-z]|[0-9]|!|#|$|%|&|'|\\*|\\+|\\-|/|=|\\?|\\^|_|`|\\{|\\||\\}|~)+(\\.([a-z]|[0-9]|!|#|$|%|&|'|\\*|\\+|\\-|/|=|\\?|\\^|_|`|\\{|\\||\\}|~)+)*)@((((([a-z]|[0-9])([a-z]|[0-9]|\\-){0,61}([a-z]|[0-9])\\.))*([a-z]|[0-9])([a-z]|[0-9]|\\-){0,61}([a-z]|[0-9])\\.(af|ax|al|dz|as|ad|ao|ai|aq|ag|ar|am|aw|au|at|az|bs|bh|bd|bb|by|be|bz|bj|bm|bt|bo|ba|bw|bv|br|io|bn|bg|bf|bi|kh|cm|ca|cv|ky|cf|td|cl|cn|cx|cc|co|km|cg|cd|ck|cr|ci|hr|cu|cy|cz|dk|dj|dm|do|ec|eg|sv|gq|er|ee|et|eu|fk|fo|fj|fi|fr|gf|pf|tf|ga|gm|ge|de|gh|gi|gr|gl|gd|gp|gu|gt| gg|gn|gw|gy|ht|hm|va|hn|hk|hu|is|in|id|ir|iq|ie|im|il|it|jm|jp|je|jo|kz|ke|ki|kp|kr|kw|kg|la|lv|lb|ls|lr|ly|li|lt|lu|mo|mk|mg|mw|my|mv|ml|mt|mh|mq|mr|mu|yt|mx|fm|md|mc|mn|ms|ma|mz|mm|na|nr|np|nl|an|nc|nz|ni|ne|ng|nu|nf|mp|no|om|pk|pw|ps|pa|pg|py|pe|ph|pn|pl|pt|pr|qa|re|ro|ru|rw|sh|kn|lc|pm|vc|ws|sm|st|sa|sn|cs|sc|sl|sg|sk|si|sb|so|za|gs|es|lk|sd|sr|sj|sz|se|ch|sy|tw|tj|tz|th|tl|tg|tk|to|tt|tn|tr|tm|tc|tv|ug|ua|ae|gb|uk|us|um|uy|uz|vu|ve|vn|vg|vi|wf|eh|ye|zm|zw|com|edu|gov|int|mil|net|org|biz|info|name|pro|aero|coop|museum|arpa))|(((([0-9]){1,3}\\.){3}([0-9]){1,3}))|(\\[((([0-9]){1,3}\\.){3}([0-9]){1,3})\\])))$";
	static final String MOBILE_DEFAULT_RULE = "^(?:\\+(?:93|355|213|1684|376|244|1264|1268|54|374|297|61|43|994|1242|973|880|1246|375|32|501|1441|975|591|387|267|55|1284|673|359|226|95|855|1345|56|86|57|269|242|506|385|357|420|243|45|253|1767|1809|1829|593|20|503|240|291|372|251|358|33|596|594|689|241|220|995|49|233|350|30|299|1473|590|502|224|509|504|852|36|354|91|62|98|964|353|970|972|39|225|1876|81|962|7|254|82|377|965|996|856|371|961|266|231|218|423|370|352|853|389|261|265|60|223|356|1670|596|222|230|262|52|373|377|382|1664|212|258|95|264|31|599|1869|64|505|234|47|968|92|507|595|51|63|48|351|1939|974|262|40|7|670|966|221|381|248|232|65|421|386|677|27|34|94|596|1869|1758|596|508|1784|249|597|268|46|41|963|886|255|66|1868|216|90|993|1649|971|44|256|380|598|1340|998|58|84|685|967|260|263))?(?:[0-9]{1,})$";
	
	private String usernameRule = USERNAME_DEFAULT_RULE;
	private String passwordRule = USERNAME_DEFAULT_RULE;
	private String emailRule = USERNAME_DEFAULT_RULE;
	private String mobileRule = USERNAME_DEFAULT_RULE;
	
	Pattern usernamePattern = Pattern.compile(USERNAME_DEFAULT_RULE);
	Pattern passwordPattern = Pattern.compile(PASSWORD_DEFAULT_RULE);
	Pattern emailPattern = Pattern.compile(EMAIL_DEFAULT_RULE);
	Pattern mobilePattern = Pattern.compile(MOBILE_DEFAULT_RULE);

	public void setUsernameRule(String rule) {
		usernameRule = rule;
		usernamePattern = Pattern.compile(rule);
	}
	
	public String getUsernameRule() {
		return usernameRule;
	}
	
	public void setPasswordRule(String rule) {
		passwordRule = rule;
		passwordPattern = Pattern.compile(rule);
	}
	
	public String getPasswordRule() {
		return passwordRule;
	}
	
	public void setEmailRule(String rule) {
		emailRule = rule;
		emailPattern = Pattern.compile(rule);
	}
	
	public String getEmailRule() {
		return emailRule;
	}
	
	public void setMobileRule(String rule) {
		mobileRule = rule;
		mobilePattern = Pattern.compile(rule);
	}
	
	public String getMobileRule() {
		return mobileRule;
	}
	
	public boolean isValidEmail(String input){
		if(input==null || "".equals(input)) return false;

		return emailPattern.matcher(input.trim().toLowerCase()).find();
	}

	public boolean isValidMobile(String input){
		if(input==null || "".equals(input)) return false;

		return mobilePattern.matcher(input.trim().toLowerCase()).find();
	}

	public boolean isValidUsername(String input){
		if(input==null || "".equals(input)) return false;

		return usernamePattern.matcher(input).find() ? true : isValidEmail(input);
	}

	public boolean isValidPassword(String input){
		if(input==null || "".equals(input)) return false;

		return passwordPattern.matcher(input).find();
	}

	@SuppressWarnings("rawtypes")
	public void setProperties(Dictionary properties) {
		if(properties==null)
			return;
		
		Enumeration e = properties.keys();
		while(e.hasMoreElements()) {
			String key = (String)e.nextElement();
			if("username-rule".equals(key))
				setUsernameRule((String)properties.get(key));
			if("password-rule".equals(key))
				setPasswordRule((String)properties.get(key));
			if("email-rule".equals(key))
				setEmailRule((String)properties.get(key));
			if("mobile-rule".equals(key))
				setMobileRule((String)properties.get(key));
		}
	}
}