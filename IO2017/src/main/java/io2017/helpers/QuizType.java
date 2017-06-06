package io2017.helpers;

public enum QuizType {
	
	CLOSED("ABCD",0), OPEN("OTWARTY",1);
	
	
	private String name;
	private int number;
		
	private QuizType(String name, int number) {
		this.name = name;
		this.number = number;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNumber() {
		return this.number;
	}
	
}
