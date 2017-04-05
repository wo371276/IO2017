package io2017.exceptions;

public class CategoryExistsException extends Exception {
	
	private static final long serialVersionUID = -1280234860519591761L;

	@Override
	public String toString() {
		return "Kategoria o tej nazwie już istnieje.";
	}
}
