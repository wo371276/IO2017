package io2017.dictonaries;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io2017.categories.CategoriesRepository;
import io2017.categories.Category;
import io2017.helpers.Language;
import io2017.users.User;
import io2017.users.UserRepository;

@Controller
public class DictionaryController {
	
	DictionaryRepository dictionaryRepository;
	UserRepository userRepository;
	CategoriesRepository categoriesRepository;
	WordRepository wordRepository;
	
	@Autowired
	public DictionaryController(DictionaryRepository dictionaryRepository, 
						UserRepository userRepository,
						CategoriesRepository categoriesRepository,
						WordRepository wordRepository) {
		this.dictionaryRepository = dictionaryRepository;
		this.userRepository = userRepository;
		this.categoriesRepository = categoriesRepository;
		this.wordRepository = wordRepository;
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
	 public String saveDictionary(@ModelAttribute("dictionary") DictionaryDto dictionaryDto,
			 @RequestParam("language")  String language, @RequestParam("category") Long categoryId ) {
		dictionaryDto.toString();
		
		//TODO moze jakas walidacja poprawnosci danych, tzn trzeba chyba tylko nazwe czy jest unique
		String username = dictionaryDto.getUser().getUserName();
		User user = userRepository.findByUserName(username);
		dictionaryDto.setUser(user);
		
		Dictionary dictionary = dictionaryDto.buildNewDictionary();
		
		dictionaryRepository.save(dictionary);
		
    	return "redirect:" + "/dictionaries";
	 }
	 
	 @RequestMapping("/dictionaries")
	public String listMyDictionaries(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userMail = auth.getName(); // bo w tej metodzie auth zwraca email
        User user = userRepository.findByEmail(userMail);
        
		List<Dictionary> dictionaries = (List<Dictionary>)dictionaryRepository.findByUser(user);
		model.addAttribute("dictionaries", dictionaries);
		
		return "admin_dictionaries";
	}
	 
	 @RequestMapping("/dictionaries/editWords")
	 public String editWords(Model model, @RequestParam("id") long dictionaryId) {
		 Dictionary dictionary = dictionaryRepository.findOne(dictionaryId);
		 List<Word> wordsList = wordRepository.findByDictionary(dictionary);
		 model.addAttribute("wordsList", wordsList);
		 model.addAttribute("dictionaryId", dictionaryId);
		 model.addAttribute("dictionaryName", dictionary.getName());
		 
		 return "edit_words";
	 }
	 
	 @RequestMapping("/dictionaries/editWords/deleteWords")
	 public String deleteWords(Model model, @RequestParam("id") long wordId,
			 					@RequestParam("return") long dictionaryId) {
		 wordRepository.delete(wordId);
		 
		 return "redirect:" + "/dictionaries/editWords?id=" + String.valueOf(dictionaryId);
	 }
	 
	 @RequestMapping("/dictionaries/deleteDictionary")
	 public String deleteDictionary(Model model, @RequestParam("id") long dictionaryId) {
		 dictionaryRepository.delete(dictionaryId);
		 // słowa tego słownika dzieki foreign key same się usuwają
		 
		 return "redirect:" + "/dictionaries";
	 }
	 
	 @RequestMapping("/dictionaries/editWords/newWords")
	 public String newWords(Model model, @RequestParam("id") long dictionaryId) {
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
	 
	 //TODO edycja istniejących słówek
	 //TODO edycja istniejących słowników
	 
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
