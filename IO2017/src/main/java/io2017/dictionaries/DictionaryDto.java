package io2017.dictionaries;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io2017.categories.Category;
import io2017.users.User;
import io2017.validators.ValidDictionaryName;

public class DictionaryDto {
	private Long dictionaryId;
	private Category category;
	private User user;
	
	@ValidDictionaryName
	@NotNull
	@Size(min=2, max=30, message="Nazwa musi mieć co najmniej 2 i co najwyżej 30 znaków")
	@NotEmpty(message="Pole nie może być puste")
	private String name;
	
	private String language;
	private Set<Word> words;
	
	private int difficulty;
	
	public DictionaryDto(Long dictionaryId, Category category, User user, String name, String language,
			Set<Word> words, int difficulty) {
		super();
		this.dictionaryId = dictionaryId;
		this.category = category;
		this.user = user;
		this.name = name;
		this.language = language;
		this.words = words;
		this.difficulty = difficulty;
	}

	public DictionaryDto() {
		
	}

	public DictionaryDto(Dictionary dictionary) {
		this.dictionaryId = dictionary.getDictionaryId();
		this.category = dictionary.getCategory();
		this.user = dictionary.getUser();
		this.name = dictionary.getName();
		this.language = dictionary.getLanguage();
		this.words = dictionary.getWords();
		this.difficulty = dictionary.getDifficulty();
	}

	public Long getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(Long dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Set<Word> getWords() {
		return words;
	}

	public void setWords(Set<Word> words) {
		this.words = words;
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public Dictionary buildNewDictionary() {
		Dictionary dictionary = new Dictionary();
		dictionary.setCategory(this.getCategory());
		dictionary.setName(this.name);
		dictionary.setUser(this.user);
		dictionary.setLanguage(this.language);
		dictionary.setDifficulty(this.difficulty);
		
		return dictionary;
	}
	
}
