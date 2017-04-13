package io2017.dictonaries;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io2017.categories.CategoriesRepository;
import io2017.categories.Category;
import io2017.users.User;
import io2017.users.UserRepository;

@Controller
public class DictionaryController {
	
	DictionaryRepository dictionaryRepository;
	UserRepository userRepository;
	CategoriesRepository categoriesRepository;
	
	@Autowired
	public DictionaryController(DictionaryRepository dictionaryRepository, 
						UserRepository userRepository,
						CategoriesRepository categoriesRepository) {
		this.dictionaryRepository = dictionaryRepository;
		this.userRepository = userRepository;
		this.categoriesRepository = categoriesRepository;
	}
	
	@RequestMapping("/admin/dictionaries")
	public String listAdminDictionaries(Model model) {
		List<Dictionary> dictionaries = (List<Dictionary>)dictionaryRepository.findAll();
		model.addAttribute("dictionaries", dictionaries);
		
		return "admin_dictionaries";
	}
	
	 @RequestMapping("/dictionaries/newDictionary")
	    public String createDictionary(Model model) {
		 
	    	DictionaryDto dictionaryDto = new DictionaryDto();
	    	
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String userMail = auth.getName(); // bo w tej metodzie auth zwraca email
	        User user = userRepository.findByEmail(userMail);
	        dictionaryDto.setUser(user);

	        List<Category> allCategories = (List<Category>) categoriesRepository.findAll();
	        
	        //TODO dodać język i enum języka
	        //TODO obsłużyć submit
	        
	    	model.addAttribute("allCategories", allCategories);
	    	model.addAttribute("dictionary", dictionaryDto);
	    	
	    	return "create_dictionary";
	    	
	 }
}
