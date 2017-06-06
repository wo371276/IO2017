package io2017.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io2017.dictionaries.Word;
import io2017.helpers.QuizMode;

public class Question {
	
	protected String query;

	protected String answer;
	
	public Question() {
		
	}
	
	public Question(String query, String answer) {
		this.query = query;
		this.answer = answer;
	}

	public String getQuery() {
		return query;
	}

	public String getAnswer() {
		return answer;
	}

	public static List<Question> getQuerySet(List<Word> words, int number, QuizMode qMode) {
		
		List<Question> questions = new ArrayList<Question>();
		
		if (words.size() < 1)
			return questions;
		
		Collections.shuffle(words);
		
		List<Word> preQuestions = words.subList(0, number);
		
		String temp_query;
		String temp_answer;
		
		for (Word pQ : preQuestions) {
			if (qMode == QuizMode.FROM_POLISH) {
				temp_query = pQ.getPolishWord();
				temp_answer = pQ.getForeignTranslation();
			} else {
				temp_query = pQ.getForeignTranslation();
				temp_answer = pQ.getPolishWord();
			}
			questions.add(new Question(temp_query,temp_answer));
		}
		
		return questions;
	}

}
