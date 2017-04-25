package io2017;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io2017.categories.CategoriesRepository;
import io2017.categories.Category;
import io2017.categories.CategoryService;
import io2017.dictionaries.Dictionary;
import io2017.dictionaries.DictionaryRepository;
import io2017.helpers.Language;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CategoriesRepository categoriesRepository;
    
    @Autowired
    private DictionaryRepository dictionaryRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Tutaj można się zalogować")));
    }
    
    
    @Test
    public void testDeleteCategory() throws Exception {
    	String firstCategoryName = "kategoria nr 1";
    	String secondCategoryName = "kategoria nr 2";
    	String dictionaryName = "nazwa slownika";
    	
    	if(categoriesRepository.findByName(firstCategoryName) == null) {
	    	Category category1 = new Category();
	    	category1.setName(firstCategoryName);
    		categoriesRepository.save(category1);
    	}
    	
    	if(categoriesRepository.findByName(secondCategoryName) == null) {
        	Category category2 = new Category();
        	category2.setName(secondCategoryName);
        	
    		categoriesRepository.save(category2);
    	}
    	
    	Category cat = categoriesRepository.findByName(secondCategoryName);
    	
    	if(dictionaryRepository.findByName(dictionaryName) == null) {
    		Dictionary newDictionary = new Dictionary();
        	newDictionary.setCategory(cat);
        	newDictionary.setLanguage(Language.ENGLISH.getName());
        	newDictionary.setName(dictionaryName);
        	newDictionary.setUser(null);
    		dictionaryRepository.save(newDictionary);
    	} else {
    		Category categoryToSet = categoriesRepository.findByName(secondCategoryName);
    		Dictionary dic = dictionaryRepository.findByName(dictionaryName);
    		dic.setCategory(categoryToSet);
    		dictionaryRepository.save(dic);
    	}
    	
    	Dictionary dictionary = dictionaryRepository.findByName(dictionaryName);
    	String dictionaryCategoryName = dictionary.getCategory().getName();
    	
    	//teraz słownik jest w kategorii nr 2
    	Assert.assertEquals(secondCategoryName, dictionaryCategoryName);
    	
    	categoryService.changeCategoriesForDelete(categoriesRepository.findByName(secondCategoryName).getCategoryId());
    	categoryService.deleteCategory(categoriesRepository.findByName(secondCategoryName).getCategoryId());
    	
    	//usunęliśmy kategorię nr 2, więc słownik powinien być w kategorii o najniższym id
    	List<Category> allCategories = (List<Category>) categoriesRepository.findAll();
    	
    	Long lowestCategoryId = (long) 999999;
    	
    	for(Category c : allCategories) {
    		lowestCategoryId = Math.min(lowestCategoryId, c.getCategoryId());
    	}
    	
    	Category lowestCategory = categoriesRepository.findOne(lowestCategoryId);
    	
    	dictionary = dictionaryRepository.findByName(dictionaryName);
    	dictionaryCategoryName = dictionary.getCategory().getName();
    	
    	Assert.assertEquals(lowestCategory.getName(), dictionaryCategoryName);
    }
    
}
