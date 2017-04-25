package io2017.categories;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io2017.dictionaries.Dictionary;
import io2017.dictionaries.DictionaryRepository;

@Service("categoryService")
public class CategoryService implements Service {
	CategoriesRepository categoriesRepository;
	DictionaryRepository dictionaryRepository;
	
	@Autowired
	public CategoryService(CategoriesRepository categoriesRepository,
			DictionaryRepository dictionaryRepository) {
		this.categoriesRepository = categoriesRepository;
		this.dictionaryRepository = dictionaryRepository;
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public void deleteCategory(long id) {
		 categoriesRepository.delete(id);
	}
	
	/*
	 * Usunięcie kategorii powoduje, że wszystkie jej słowniki
	 * zostaną przepisane do kategorii o najmniejszym id
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public void changeCategoriesForDelete(Long id) {
		 List<Category> allCategories = (List<Category>) categoriesRepository.findAll();
		 
		 Long minId = null;
		 
		 for(Category category : allCategories) {
			 Long categoryId = category.getCategoryId();
			 
			 if(minId == null && categoryId != id) {
				 minId = categoryId;
			 } else {
				 if(categoryId != id) {
					 minId = Math.min(categoryId, minId);
				 }
			 }
		 }
		 
		 if(minId != null) {
	         Category defaultCategory = categoriesRepository.findOne(minId);
	         Category categoryToRemove = categoriesRepository.findOne(id);
	         Set<Dictionary> dictionaries = categoryToRemove.getDictionaries();
	         
	         for(Dictionary dictionary : dictionaries) {
	             dictionary.setCategory(defaultCategory);
	             //categoryToRemove.deleteFromDictionaries(dictionary);
	             dictionaryRepository.save(dictionary);
	         }
	         categoryToRemove.clearDictionaries();
	     }
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String value() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
