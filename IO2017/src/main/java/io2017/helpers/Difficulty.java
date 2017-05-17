package io2017.helpers;

import java.util.LinkedList;
import java.util.List;

public enum Difficulty {
	no1(1), no2(2), no3(3), no4(4), no5(5);
	
	int difficulty;
	
	private Difficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getDifficulty() {
		return this.difficulty;
	}
	
	public static List<Difficulty> getAllDifficulties() {
		List<Difficulty> ret = new LinkedList<Difficulty>();
		
		ret.add(Difficulty.no1);
		ret.add(Difficulty.no2);
		ret.add(Difficulty.no3);
		ret.add(Difficulty.no4);
		ret.add(Difficulty.no5);
		
		return ret;
	}
}
