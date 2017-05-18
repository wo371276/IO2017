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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	@Column(name="score")
	private int score;
	
	@Column(name="mode")
	private int mode;
	
	@ManyToOne
	@JoinColumn(name = "dictionary_id")
	private Dictionary dictionary;
	

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Score(int score, int mode, Dictionary dictionary, User user) {
		this.score = score;
		this.mode = mode;
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


	public int getMode() {
		return mode;
	}


	public void setMode(int mode) {
		this.mode = mode;
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
