package io2017.quiz;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io2017.dictionaries.Word;

public class QuestionABCD {
	
	private String query;

	private String answer;
	
	private String a;

	private String b;

	private String c;

	private String d;
	
	public QuestionABCD(Word pQ, Set<String> preAnswers, Integer mode) {
		if (mode == 0) {
			query = pQ.getPolishWord();
			answer = pQ.getForeignTranslation();
		} else {
			query = pQ.getForeignTranslation();
			answer = pQ.getPolishWord();
		}
		
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

	public String getQuery() {
		return query;
	}

	public String getAnswer() {
		return answer;
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

	public static List<QuestionABCD> getQuerySet(List<Word> words, Integer number, Integer mode) {
		
		List<QuestionABCD> questions = new LinkedList<QuestionABCD>();

		if (words.size() < 4)
			return questions;
		
		Set<String> answers = new HashSet<String>();
		for (Word word : words) {
			if (mode == 0) {
				answers.add(word.getForeignTranslation());
			} else {
				answers.add(word.getPolishWord());
			}
		}
		
		Collections.shuffle(words);
		
		List<Word> preQuestions = words.subList(0, number);
				
		for (Word pQ : preQuestions) {
			questions.add(new QuestionABCD(pQ,answers,mode));
		}
		
		return questions;
	}

}
