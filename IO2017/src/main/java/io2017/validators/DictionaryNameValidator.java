package io2017.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import io2017.categories.CategoriesRepository;
import io2017.dictionaries.DictionaryRepository;

public class DictionaryNameValidator 
	implements ConstraintValidator<ValidDictionaryName, String> {
	private DictionaryRepository dictionariesRepository;
	
	@Autowired
	public DictionaryNameValidator(DictionaryRepository dictionariesRepository) {
		this.dictionariesRepository = dictionariesRepository;
	}
	
	@Override
	public void initialize(ValidDictionaryName constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if(dictionariesRepository.findByName(value) == null) {
			//przypadek gdy znajdziemy, ale to byliśmy my sami przed zmianą
			return true;
		} else {
			return false;
		}
	}
	

}