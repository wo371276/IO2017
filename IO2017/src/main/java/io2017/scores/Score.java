package io2017.scores;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io2017.dictionaries.Dictionary;
import io2017.users.User;

@Entity
@Table(name = "scores")
public class Score implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6390166773481421497L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	@Column(name="score")
	private int score;
	
	@Column(name="quizType")
	private String quizType;
	
	@ManyToOne
	@JoinColumn(name = "dictionary_id")
	private Dictionary dictionary;
	

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Score(int score, String quizType, Dictionary dictionary, User user) {
		this.score = score;
		this.quizType = quizType;
		this.dictionary = dictionary;
		this.user = user;
	}
	
	public Score() {
		
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public String getQuizType() {
		return quizType;
	}


	public void setQuizType(String quizType) {
		this.quizType = quizType;
	}


	public Dictionary getDictionary() {
		return dictionary;
	}


	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
}
