package io2017.dictonaries;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io2017.categories.Category;
import io2017.users.User;


@Entity
@Table(name = "dictonaries")
public class Dictionary implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="dictionaryid")
	private Long dictionaryId;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@NotNull
	@Size(min=2, max=30)
	@Column(name = "name", unique = true)
	private String name;
	
	@NotNull
	@Column(name = "language")
	private String language;
	
	@OneToMany(mappedBy = "dictionary", cascade = CascadeType.ALL)
	private Set<Word> words;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
