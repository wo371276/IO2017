package io2017.exceptions;

public class DictionaryExistsException extends Exception {

	private static final long serialVersionUID = -2553271576568777695L;
	
	@Override
	public String toString() {
		return "Słownik o tej nazwie już istnieje";
	}
	
}
