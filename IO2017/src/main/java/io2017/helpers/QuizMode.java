package io2017.helpers;

public enum QuizMode {
	
	FROM_POLISH("fromPolish"), TO_POLISH("toPolish");
	
	
	private String name;
		
	private QuizMode(String name) {
		this.name = name;
	}
	
	public static QuizMode getQuizMode(String name) {
		if(name.equals(QuizMode.FROM_POLISH.name)) {
			return QuizMode.FROM_POLISH;
		} else if(name.equals(QuizMode.TO_POLISH.name)) {
			return QuizMode.TO_POLISH;
		}
		
		return QuizMode.FROM_POLISH;
	}
	
}
