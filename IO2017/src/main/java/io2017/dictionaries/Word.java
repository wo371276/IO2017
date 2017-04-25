package io2017.dictionaries;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "words")
public class Word implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7231295020251785358L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="wordid")
	private Long wordId;
	
	@ManyToOne
	@JoinColumn(name = "dictionary_id")
	private Dictionary dictionary;
	
	@NotNull
	@Column(name = "polish_word")
	private String polishWord;
	
	@Column(name = "foreign_translation")
	private String foreignTranslation;

	public Long getWordId() {
		return wordId;
	}

	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getPolishWord() {
		return polishWord;
	}

	public void setPolishWord(String polishWord) {
		this.polishWord = polishWord;
	}

	public String getForeignTranslation() {
		return foreignTranslation;
	}

	public void setForeignTranslation(String foreignTranslation) {
		this.foreignTranslation = foreignTranslation;
	}
	
	
}
