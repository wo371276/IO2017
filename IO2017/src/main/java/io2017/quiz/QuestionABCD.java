package io2017.quiz;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io2017.dictionaries.Word;
import io2017.helpers.QuizMode;

public class QuestionABCD extends Question {
	
	private String a;

	private String b;

	private String c;

	private String d;
	
	public QuestionABCD(String query, String answer, Set<String> preAnswers) {
		super();
		
		this.query = query;
		this.answer = answer;
		
		System.out.println(preAnswers.remove(answer));
		List<String> list = new LinkedList<String>(preAnswers);
		Collections.shuffle(list);
		List<String> answers = list.subList(0, 3);
		answers.add(answer);
		Collections.shuffle(answers);

		a = answers.get(0);
		b = answers.get(1);
		c = answers.get(2);
		d = answers.get(3);
		
		preAnswers.add(answer);
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

	public static List<Question> getQuerySet(List<Word> words, Integer number, QuizMode qMode) {
		
		List<Question> questions = new LinkedList<Question>();

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
				
		for (Word pQ : preQuestions) {
			if (qMode == QuizMode.FROM_POLISH) {
				questions.add(new QuestionABCD(pQ.getPolishWord(),pQ.getForeignTranslation(),answers));
			} else {
				questions.add(new QuestionABCD(pQ.getForeignTranslation(),pQ.getPolishWord(),answers));
			}
		}
		
		return questions;
	}

}
