package io2017.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io2017.dictionaries.Word;
import io2017.helpers.QuizMode;

public class QuestionABCD extends Question {
	
	private String a;

	private String b;

	private String c;

	private String d;
	
	public QuestionABCD(String query, String answer, List<String> alternatives) {
		super();
		
		if (alternatives.size() != 3) {
			throw new IllegalArgumentException("The size of alternatives must be equal 3.");
		}
		
		this.query = query;
		this.answer = answer;
		
		alternatives.add(answer);
		Collections.shuffle(alternatives);

		a = alternatives.get(0);
		b = alternatives.get(1);
		c = alternatives.get(2);
		d = alternatives.get(3);
	}

	public String getA() {
		return a;
	}

	public String getB() {
		return b;
	}

	public String getC() {
		return c;
	}

	public String getD() {
		return d;
	}

	public static List<Question> getQuerySet(List<Word> words, int number, QuizMode qMode) {
		
		List<Question> questions = new ArrayList<Question>();

		if (words.size() < 4)
			return questions;
		
		Set<String> answers = new HashSet<String>();
		for (Word word : words) {
			if (qMode == QuizMode.FROM_POLISH) {
				answers.add(word.getForeignTranslation());
			} else {
				answers.add(word.getPolishWord());
			}
		}
		
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
			
			answers.remove(temp_answer);
			List<String> list = new ArrayList<String>(answers);
			Collections.shuffle(list);
			list = list.subList(0, 3);
			questions.add(new QuestionABCD(temp_query,temp_answer,list));
			answers.add(temp_answer);
		}
		
		return questions;
	}

}
