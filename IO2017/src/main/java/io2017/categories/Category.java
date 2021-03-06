package io2017.categories;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io2017.dictionaries.Dictionary;

@Entity
@Table(name = "categories")
public class Category implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8705813358609592339L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="categoryid")
	private Long categoryId;
	
	@NotNull
	@Size(min=2, max=30)
	@Column(name = "name", unique = true)
	private String name;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private Set<Dictionary> dictionaries;
	
	public Category() {
		
	}
	
	public Category(Category category) {
		this.categoryId = category.categoryId;
		this.name = category.name;
	}
	
	public Category(CategoryDto categoryDto) {
		this.name = categoryDto.getName();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(Set<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}
	
	public void deleteFromDictionaries(Dictionary dictionary) {
		this.dictionaries.remove(dictionary);
	}
	
	public void clearDictionaries() {
		this.dictionaries.clear();
	}
	public Set<Dictionary> getDictionaries(boolean all, String language){
		if (all)
			return getDictionaries();
		return dictionaries.stream().filter(x -> x.getLanguage().equals(language))
				.collect(Collectors.toSet());
	}
	
}