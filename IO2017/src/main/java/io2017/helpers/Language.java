package io2017.helpers;

import java.util.LinkedList;
import java.util.List;

public enum Language {
	
	ENGLISH("ANGIELSKI"), FRENCH("FRANCUSKI"), GERMAN("NIEMIECKI");
	
	
	private String name;
	
	private Language(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static List<Language> getAllLanguages() {
		List<Language> languages= new LinkedList<Language>();

		languages.add(Language.ENGLISH);
		languages.add(Language.FRENCH);
		languages.add(Language.GERMAN);
		
		return languages;		
	}
	
}
