package io2017.exceptions;

public class UserExistsException extends Exception {
	
	public UserExistsException() {}

	/**
	 * 
	 */
	private static final long serialVersionUID = 35009164236914411L;
	
	@Override
	public String toString() {
		return "Istnieje już użytkownik o takim loginie";
	}
}
