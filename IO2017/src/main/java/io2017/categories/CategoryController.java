package io2017.categories;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import io2017.exceptions.CategoryExistsException;

@Controller
public class CategoryController {
	
	CategoriesRepository categoriesRepository;
	
	@Autowired
	public CategoryController(CategoriesRepository categoriesRepository) {
		this.categoriesRepository = categoriesRepository;
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
	 
	 @RequestMapping("/admin/categories/deleteCategory")
	 public String deleteCategory(Model model, @RequestParam("id") long id) {
		categoriesRepository.delete(id);
    	/*
    	 * TODO jak już będą słowniki, to po usunięci kategorii
    	 * trzeba przepiać wszytskie jej słowniki do kategorii default
    	 */
		
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
