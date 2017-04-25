package io2017.exceptions;

public class EmptyPasswordException extends Exception {
	
	private static final long serialVersionUID = -7760257998782849384L;

	@Override
	public String toString() {
		return "Pole z hasłem nie może być puste.";
	}
}
