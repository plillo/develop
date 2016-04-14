package it.hash.osgi.user.password;

import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

import it.hash.osgi.utils.Random;

public class PasswordImpl implements Password {
    private static final int iterations = 20*10/*00*/;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;
    
	@Override
    public String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        
        // store the salt with the hashed password
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }
    
	@Override
    public boolean check(String password, String stored) throws Exception{
		if(password==null || stored==null)
			return false;
		
		String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalStateException(
                "The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }

	@Override
	public String getRandom() {
		return Random.getRandomKey(8);
	}

    private String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return Base64.encodeBase64String(key.getEncoded());
    }

}
