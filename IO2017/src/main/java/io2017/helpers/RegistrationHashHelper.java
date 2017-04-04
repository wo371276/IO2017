package io2017.helpers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationHashHelper {
	String code;
	long id;
	String hashedEmail;
	final String pattern = "(\\d+)_(.*)";
	
	boolean correctPattern;
	public RegistrationHashHelper(String code) {
		this.code = code;
		this.checkPattern();
	}
	
	public boolean isCorrectPattern() {
		return this.correctPattern;
	}
	
	public long getId() {
		return id;
	}

	public String getHashedEmail() {
		return hashedEmail;
	}
	
    public String makeSHA1Hash(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();

            String hexStr = "";
            for (int i = 0; i < digest.length; i++) {
                hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
            }
            return hexStr;
        }

	private void checkPattern() {
		String pattern = "(\\d+)\\_(.*)";

	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(code);

	      if (m.find( )) {
	    	  correctPattern = true;
	         id = (long) Integer.valueOf(m.group(1));
	         hashedEmail = m.group(2);
	      }else {
	    	  correctPattern = false;
	      }
	}

}
