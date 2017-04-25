package io2017.categories;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import io2017.dictionaries.Dictionary;
import io2017.dictionaries.DictionaryRepository;
import io2017.exceptions.CategoryExistsException;

@Controller
public class CategoryController {
	
	CategoryService categoryService;
	CategoriesRepository categoriesRepository;
	DictionaryRepository dictionaryRepository;
	
	@Autowired
	public CategoryController(CategoriesRepository categoriesRepository,
			DictionaryRepository dictionaryRepository,
			CategoryService categoryService) {
		this.categoriesRepository = categoriesRepository;
		this.dictionaryRepository = dictionaryRepository;
		this.categoryService = categoryService;
	}
	
	@RequestMapping("/admin/categories")
	public String listCategories(Model model) {
		List<Category> categories = (List<Category>) categoriesRepository.findAll();
		model.addAttribute("categories", categories);
		
		return "admin_categories";
	}
	
	 @RequestMapping("/admin/categories/newCategory")
	    public String createCategory(Model model) {
	    	CategoryDto categoryDto = new CategoryDto();
	    	model.addAttribute("category", categoryDto);
	    	
	    	return "create_category";
	 }
	 
	 @RequestMapping("/admin/categories/newCategory/submit")
	 public String saveCategory(@ModelAttribute("category") @Valid CategoryDto categoryDto, 
		    	BindingResult bindingResult, 
		    	WebRequest request, 
		    	Errors errors) {
		if(bindingResult.hasErrors() == true) {
			// TODO może przerobić adnotację valid na testowanie za pomocą CategoryExistsException ?
			return "create_category";
		} else {
			Category category = new Category(categoryDto);
			categoriesRepository.save(category);
		}
		
	    
    	return "redirect:" + "/admin/categories";
	 }
	 
	 /*
	  * Usunięcie kategorii powoduje, że wszystkie jej słowniki
	  * zostaną przepisane do kategorii o najmniejszym id
	  */
	 @RequestMapping("/admin/categories/deleteCategory")
	 @Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE )
	 public String deleteCategory(Model model, @RequestParam("id") long id) {
		 categoryService.changeCategoriesForDelete(id);
		 categoryService.deleteCategory(id);
		 
		 return "redirect:" + "/admin/categories";
	 }

	 @RequestMapping("/admin/categories/editCategory")
	 public String editCategory(Model model, @RequestParam("id") long id) {
		 Category category = categoriesRepository.findOne(id);
		 CategoryDto categoryDto = new CategoryDto(category);
		 
		 model.addAttribute("category", categoryDto);
		 
		 return "edit_category";
	 }
	 
	 @RequestMapping("/admin/categories/editCategory/submit")
	 public String saveEditedCategory(@ModelAttribute("category") CategoryDto categoryDto,
			 			BindingResult bindingResult) {
    	//TODO dodać sprawdzanie czy nowy tytuł nie jest pusty
		try {
			saveEditedCategory(categoryDto);
		} catch (CategoryExistsException e) {
			bindingResult.rejectValue("name",  "message.regError", e.toString());
			
			return "edit_category";
		}
		
    	return "redirect:" + "/admin/categories";
	 }
	 
	 private boolean saveEditedCategory(CategoryDto categoryDto)
	 					throws CategoryExistsException {
		 if(categoryDto.getId() != null) {
			 Category sameNameCategory = categoriesRepository.findByName(categoryDto.getName());
			 if(sameNameCategory != null) {
				 if(categoryDto.getId().equals(sameNameCategory.getCategoryId()) == false) {
					 throw new CategoryExistsException();
				 }
			 }
		 }
		 
		 Category category = categoriesRepository.findOne(categoryDto.getId());
		 category.setName(categoryDto.getName());
		 
		 categoriesRepository.save(category);
		 
		 return true;
	 }
	 
	 @RequestMapping("/category")
	 public String showCategoryForUser(Model model, @RequestParam("id") long id) {
		 Category category = categoriesRepository.findOne(id);
		 
		 model.addAttribute("category", category);
		
    	return "category";
	 }
	 
	 @RequestMapping("/categories")
	public String listCategoriesForUser(Model model) {
		List<Category> categories = (List<Category>) categoriesRepository.findAll();
		model.addAttribute("categories", categories);
		
		return "categories";
	}

}
