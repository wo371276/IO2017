package io2017.helpers;

public enum Difficulty {
	no1(1), no2(2), no3(3), no4(4), no5(5);
	
	int difficulty;
	
	private Difficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getDifficulty() {
		return this.difficulty;
	}
}
