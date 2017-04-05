package io2017.categories;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io2017.validators.ValidCategoryName;

public class CategoryDto {
	
	Long id;
	
	
	//TODO przedefiniować jakoś komunikat "size must be between 2 and 30
	@ValidCategoryName
	@NotNull
	@Size(min=2, max=30)
	@NotEmpty(message="Pole nie może być puste")
	private String name;
	
	public CategoryDto() {
		
	}
	
	public CategoryDto(String name) {
		this.name = name;
	}
	
	public CategoryDto(Category category) {
		this.name = category.getName();
		this.id = category.getCategoryId();
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
