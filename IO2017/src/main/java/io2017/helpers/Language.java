package io2017.helpers;

public enum Language {
	
	ENGLISH("ANGIELSKI"), FRENCH("FRANCUSKI"), GERMAN("NIEMIECKI");
	
	
	private String name;
	
	public static final String poPolsku = "Po polsku";
	
	private Language(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFlashCardsText() {
		if(this.equals(Language.ENGLISH)) {
			return "Po angielsku";
		} else if(this.equals(Language.FRENCH)) {
			return "Po francusku";
		} else if(this.equals(Language.GERMAN)) {
			return "Po niemiecku";
		}
		return "";
	}
	
	public static Language getLanguageType(String name) {
		if(name.equals(Language.ENGLISH.getName())) {
			return Language.ENGLISH;
		} else if(name.equals(Language.FRENCH.getName())) {
			return Language.FRENCH;
		} else if(name.equals(Language.GERMAN.getName())) {
			return Language.GERMAN;
		}
		
		return Language.ENGLISH;
	}
	
}
