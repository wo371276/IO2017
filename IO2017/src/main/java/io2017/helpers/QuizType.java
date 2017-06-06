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
	
	public static QuizType getQuizTypeByName(String name) {
		if(name.equals(QuizType.OPEN.getName())) {
			return QuizType.OPEN;
		} else if(name.equals(QuizType.CLOSED.getName())) {
			return QuizType.CLOSED;
		}
		
		return QuizType.CLOSED;
	}
	
	public static QuizType getQuizTypeByNumber(int number) {
		if(number == QuizType.OPEN.getNumber()) {
			return QuizType.OPEN;
		} else if(number == QuizType.CLOSED.getNumber()) {
			return QuizType.CLOSED;
		}
		
		return QuizType.CLOSED;
	}
	
}
