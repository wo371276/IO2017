package io2017.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import io2017.categories.CategoriesRepository;

public class CategoryNameValidator 
	implements ConstraintValidator<ValidCategoryName, String> {
	private CategoriesRepository categoriesRepository;
	
	@Autowired
	public CategoryNameValidator(CategoriesRepository categoriesRepository) {
		this.categoriesRepository = categoriesRepository;
	}
	
	@Override
	public void initialize(ValidCategoryName constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if(categoriesRepository.findByName(value) == null) {
			//przypadek gdy znajdziemy, ale to byliśmy my sami przed zmianą
			return true;
		} else {
			return false;
		}
	}
	

}