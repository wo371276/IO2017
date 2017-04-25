package io2017.exceptions;

public class EmailExistsException extends Exception {
	
	public EmailExistsException() {}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6211498797525617895L;
	
	@Override
	public String toString() {
		return "Istnieje już konto dla tego adresu mailowego";      
	}
}
