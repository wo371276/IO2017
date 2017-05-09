package io2017.quiz;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io2017.categories.CategoriesRepository;
import io2017.dictionaries.Dictionary;
import io2017.dictionaries.DictionaryRepository;
import io2017.dictionaries.Word;
import io2017.dictionaries.WordRepository;
import io2017.helpers.Language;
import io2017.users.UserRepository;
import io2017.users.UserRolesRepository;

@Controller
public class QuizOptionsController {
	DictionaryRepository dictionaryRepository;
	UserRepository userRepository;
	CategoriesRepository categoriesRepository;
	WordRepository wordRepository;
	UserRolesRepository userRolesRepository;
	
	@Autowired
	public QuizOptionsController(DictionaryRepository dictionaryRepository, 
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
	
	@RequestMapping("/quiz/options")
	public String quizOptions(Model model, @RequestParam("id") long id) {
			
		Dictionary dictionary = dictionaryRepository.findOne(id);
		List<Word> words = new LinkedList<Word>();
		words.addAll(dictionary.getWords());
		model.addAttribute("wordsNumber", words.size());
		model.addAttribute("dictionaryLanguage", dictionary.getLanguage());
		model.addAttribute("id", id);
		
		return "quiz_options";
	}
}
