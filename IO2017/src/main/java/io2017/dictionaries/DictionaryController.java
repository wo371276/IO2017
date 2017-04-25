package io2017.dictionaries;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io2017.categories.CategoriesRepository;
import io2017.categories.Category;
import io2017.categories.CategoryDto;
import io2017.exceptions.CategoryExistsException;
import io2017.exceptions.DictionaryExistsException;
import io2017.helpers.Language;
import io2017.users.User;
import io2017.users.UserRepository;
import io2017.users.UserRolesRepository;

@Controller
public class DictionaryController {
	
	DictionaryRepository dictionaryRepository;
	UserRepository userRepository;
	CategoriesRepository categoriesRepository;
	WordRepository wordRepository;
	UserRolesRepository userRolesRepository;
	
	@Autowired
	public DictionaryController(DictionaryRepository dictionaryRepository, 
						UserRepository userRepository,
						CategoriesRepository categoriesRepository,
						WordRepository wordRepository,
						UserRolesRepository userRolesRepository) {
		this.dictionaryRepository = dictionaryRepository;
		this.userRepository = userRepository;
		this.categoriesRepository = categoriesRepository;
		this.wordRepository = wordRepository;
		this.userRolesRepository = userRolesRepository;
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

    	model.addAttribute("allCategories", allCategories);
    	model.addAttribute("dictionary", dictionaryDto);
    	model.addAttribute("languages", Language.getAllLanguages());
    
    	//TODO wystylować ładniej create_dictionary
    	return "create_dictionary";
	 }
	
	 @RequestMapping("/dictionaries/newDictionary/submit")
	 public String saveDictionary(Model model, @ModelAttribute("dictionary") DictionaryDto dictionaryDto,
			 @RequestParam("language")  String language, @RequestParam("category") Long categoryId,
			 BindingResult bindingResult) {
		dictionaryDto.toString();
		
		if(bindingResult.hasErrors() == true) {
			System.out.println("there's an error");
			return "create_dictionary";
		}
		
		String username = dictionaryDto.getUser().getUserName();
		User user = userRepository.findByUserName(username);
		dictionaryDto.setUser(user);
		
		try {
			saveDictionaryDto(dictionaryDto);
		} catch (DictionaryExistsException e) {
			bindingResult.rejectValue("name",  "message.regError", e.toString());
			
	        List<Category> allCategories = (List<Category>) categoriesRepository.findAll();
			
	    	model.addAttribute("allCategories", allCategories);
	    	model.addAttribute("dictionary", dictionaryDto);
	    	model.addAttribute("languages", Language.getAllLanguages());
			
			return "create_dictionary";
		}
		
    	return "redirect:" + "/dictionaries";
	 }
	 
	 @RequestMapping("/dictionaries/editDictionary")
	    public String editDictionary(Model model, @RequestParam("id") long id) {			 
			 if(this.haveAccess(id) == false) {
				 return "redirect:" + "/403";
			 }
			 
			 Dictionary dictionary = dictionaryRepository.findOne(id);

			 DictionaryDto dictionaryDto = new DictionaryDto(dictionary);
			 
		     List<Category> allCategories = (List<Category>) categoriesRepository.findAll();

			 model.addAttribute("allCategories", allCategories);
			 model.addAttribute("dictionary", dictionaryDto);
			 model.addAttribute("languages", Language.getAllLanguages());			 
			 
			 return "edit_dictionary";
		 }
	 
	 @RequestMapping("/dictionaries/editDictionary/submit")
	 public String saveEditedDictionary(Model model, @ModelAttribute("dictionary") DictionaryDto dictionaryDto,
			 @RequestParam("language")  String language, @RequestParam("category") Long categoryId,
			 BindingResult bindingResult) {
    	//TODO dodać sprawdzanie czy nowy tytuł nie jest pusty
		try {
			saveDictionaryDto(dictionaryDto);
		} catch (DictionaryExistsException e) {
			bindingResult.rejectValue("name",  "message.regError", e.toString());
			
			List<Category> allCategories = (List<Category>) categoriesRepository.findAll();
			
	    	model.addAttribute("allCategories", allCategories);
	    	model.addAttribute("dictionary", dictionaryDto);
	    	model.addAttribute("languages", Language.getAllLanguages());
			
			return "edit_dictionary";
		}
		
    	return "redirect:" + "/dictionaries";
	 }
	
	private void saveDictionaryDto(DictionaryDto dictionaryDto)
								throws DictionaryExistsException {
		
		Dictionary dictionary;
		
		// pierwszy przypadek edycja slownika
		if(dictionaryDto.getDictionaryId() != null) {
//			 Dictionary sameNameDictionary = dictionaryRepository.findByName(dictionaryDto.getName());
//			 if(sameNameDictionary != null) {
//				 if(dictionaryDto.getDictionaryId().equals(sameNameDictionary.getDictionaryId()) == false) {
//					 throw new DictionaryExistsException();
//				 }
//			 }
			 dictionary = dictionaryRepository.findOne(dictionaryDto.getDictionaryId());
			 dictionary.setName(dictionaryDto.getName());
			 dictionary.setCategory(dictionaryDto.getCategory());
			 dictionary.setLanguage(dictionaryDto.getLanguage());
		 } else {
			 //slownik nie ma ID -> nowy slownik
			 Dictionary sameNameDictionary = dictionaryRepository.findByName(dictionaryDto.getName());
			 if(sameNameDictionary != null) {
				 throw new DictionaryExistsException();
			 }
			 dictionary = dictionaryDto.buildNewDictionary();
		 }
		
		dictionaryRepository.save(dictionary);
	}
	 
	@RequestMapping("/dictionaries")
	public String listMyDictionaries(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userMail = auth.getName(); // bo w tej metodzie auth zwraca email
        User user = userRepository.findByEmail(userMail);
        
		List<Dictionary> dictionaries = (List<Dictionary>)dictionaryRepository.findByUser(user);
		model.addAttribute("dictionaries", dictionaries);
		model.addAttribute("isNotAdminPage", "true");
		
		//celowo używam tego samego template'u co dla admin'a
		return "admin_dictionaries";
	}
	 
	 @RequestMapping("/dictionaries/editWords")
	 public String editWords(Model model, @RequestParam("id") long dictionaryId,
			 @RequestParam(value = "word_id", required = false) Long wordId) {
		 
		 if(this.haveAccess(dictionaryId) == false) {
			 return "redirect:" + "/403";
		 }
		 
		 Dictionary dictionary = dictionaryRepository.findOne(dictionaryId);
		 
		 List<Word> wordsList = wordRepository.findByDictionary(dictionary);
		 model.addAttribute("wordsList", wordsList);
		 model.addAttribute("dictionaryId", dictionaryId);
		 model.addAttribute("dictionaryName", dictionary.getName());
		 model.addAttribute("id", wordId);
		 		 
		 return "edit_words";
	 }
	 
	 @RequestMapping("/dictionaries/editWord/submit")
	 public String saveWord(Model model,
			 @RequestParam("wordId")  Long wordId,
			 @RequestParam("dictionaryId")  Long dictionaryId,
			 @RequestParam("polishWord")  String polishWord,
			 @RequestParam("foreignTranslation") String foreignTranslation) {
		 Dictionary dictionary = dictionaryRepository.findOne(dictionaryId);
		 Word word = wordRepository.findOne(wordId);
		 System.out.println(word.getPolishWord());
		 
		 word.setPolishWord(polishWord);
		 word.setForeignTranslation(foreignTranslation);
		 
		 wordRepository.save(word);
		 
		 List<Word> wordsList = wordRepository.findByDictionary(dictionary);
		 model.addAttribute("wordsList", wordsList);
		 model.addAttribute("dictionaryId", dictionaryId);
		 model.addAttribute("dictionaryName", dictionary.getName());
		 model.addAttribute("id", null);
		 
		 return "edit_words";
	 }
	 
	 @RequestMapping("/dictionaries/editWords/deleteWords")
	 public String deleteWords(Model model, @RequestParam("id") long wordId,
			 					@RequestParam("return") long dictionaryId) {
		 if(this.haveAccess(dictionaryId) == false) {
			 return "redirect:" + "/403";
		 }
		 
		 wordRepository.delete(wordId);
		 
		 return "redirect:" + "/dictionaries/editWords?id=" + String.valueOf(dictionaryId);
	 }
	 
	 @RequestMapping("/dictionaries/deleteDictionary")
	 public String deleteDictionary(Model model, @RequestParam("id") long dictionaryId) {
		 
		 if(this.haveAccess(dictionaryId) == false) {
			 return "redirect:" + "/403";
		 }
		 
		 dictionaryRepository.delete(dictionaryId);
		 // słowa tego słownika dzieki foreign key same się usuwają
		 
		 return "redirect:" + "/dictionaries";
	 }
	 
	 @RequestMapping("/dictionaries/editWords/newWords")
	 public String newWords(Model model, @RequestParam("id") long dictionaryId) {
		 
		 if(this.haveAccess(dictionaryId) == false) {
			 return "redirect:" + "/403";
		 }
		 
		 Dictionary dictionary = dictionaryRepository.findOne(dictionaryId);
		 model.addAttribute("dictionaryId", dictionaryId);
		 model.addAttribute("dictionaryName", dictionary.getName());
		 model.addAttribute("newWords", new TextArea("", dictionaryId));
		 
		 return "new_words";
	 }
	 /*
	  * podejrzenie wszystkich parametrów
	  * @RequestParam Map<String,String> allRequestParams
	  */
	 @RequestMapping("/dictionaries/editWords/newWords/submit")
	 public String saveNewWords(@RequestParam("text") String newWords,
			 @RequestParam("dictionaryId") long dictionaryId) {
		 String[] lines = newWords.split(System.getProperty("line.separator"));
		 Dictionary dictionary = dictionaryRepository.findOne(dictionaryId);
		 
		 for(String line : lines) {
			 String[] words = line.split("=");
			 //jak sie nie znajdzie = to jest ignorowane
			 if(words.length >= 2) {
				 //jak jest pare znakow = to patrzymy tylko na 1szy
				 //reszte ignorujemy
				 String foreignWord = words[0].trim(); //trim usuwa pocz/kon spacje
				 String polishTranslation = words[1].trim();
				 
				 Word newWord = new Word();
				 newWord.setDictionary(dictionary);
				 newWord.setPolishWord(polishTranslation);
				 newWord.setForeignTranslation(foreignWord);
				 wordRepository.save(newWord);			 
			 }
		 }
		 
		 return "redirect:" + "/dictionaries/editWords?id=" + String.valueOf(dictionaryId);
	 }
	 
	 /*
	  * sprawdza czy użytkownik może dokonywać zmian
	  * na słowniku o danym id
	  */
	 private boolean haveAccess(long dictionaryId) {
		 User me = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 boolean isAdmin = userRolesRepository.findRoleByUserId(me.getUserId()).get(0).equals("ROLE_ADMIN");
		 
		 Dictionary dictionary = dictionaryRepository.findOne(dictionaryId);
		 
		 if(isAdmin == false) {
			 //skoro nie admin to sprawdzamy czy to jego słownik
			 if(dictionary.getUser().getUserId().equals(me.getUserId()) != true) {
				 //skoro nie moj slownik to odrzucamy żądanie
				 return false;
			 }
		 }
		 
		 return true;
	 }
	 	 
	 private class TextArea {
		 private String text;
		 private Long dictionaryId;
		 
		 public TextArea(String text, Long dictionaryId) {
			 this.text = text;
			 this.dictionaryId = dictionaryId;
		 }

		@SuppressWarnings("unused")
		public String getText() {
			return text;
		}
		
		@SuppressWarnings("unused")
		public void setText(String text) {
			this.text = text;
		}
		
		@SuppressWarnings("unused")
		public Long getDictionaryId() {
			return dictionaryId;
		}
		
		@SuppressWarnings("unused")
		public void setDictionaryId(Long dictionaryId) {
			this.dictionaryId = dictionaryId;
		}	 
	 }
}
