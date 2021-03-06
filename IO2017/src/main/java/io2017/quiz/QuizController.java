package io2017.quiz;

import java.util.ArrayList;
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
import io2017.helpers.QuizMode;
import io2017.helpers.QuizType;
import io2017.users.UserRepository;
import io2017.users.UserRolesRepository;

@Controller
public class QuizController {
	
	DictionaryRepository dictionaryRepository;
	UserRepository userRepository;
	CategoriesRepository categoriesRepository;
	WordRepository wordRepository;
	UserRolesRepository userRolesRepository;
	
	@Autowired
	public QuizController(DictionaryRepository dictionaryRepository, 
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
	
	@RequestMapping("/quiz/flashcards")
	public String quizFlashcards(Model model, @RequestParam("id") long id,
			@RequestParam(value = "mode", required=false, defaultValue="0") int mode) {
		
		Dictionary dictionary = dictionaryRepository.findOne(id);
		List<Word> words = new ArrayList<Word>();
		words.addAll(dictionary.getWords());
		model.addAttribute("words", words);
		model.addAttribute("wordsNumber", words.size());
		model.addAttribute("dictionaryName", dictionary.getName());
		model.addAttribute("dictionaryCategory", dictionary.getCategory());
		model.addAttribute("dictionaryId", dictionary.getDictionaryId());
		boolean hasWords = words.size() != 0 ? true : false;
		model.addAttribute("hasWords", hasWords);
		
		String headText = Language.getLanguageType(dictionary.getLanguage()).getFlashCardsText();
		String poPolsku = Language.poPolsku;
		if(mode == 0) {
			model.addAttribute("firstHeader", poPolsku);
			model.addAttribute("secondHeader", headText);
		} else {
			model.addAttribute("firstHeader", headText);
			model.addAttribute("secondHeader", poPolsku);
		}
		
		model.addAttribute("mode", mode);
		model.addAttribute("changeMode", 1 - mode);
		
		return "quiz_flashcard";
	}
	
	@RequestMapping("/quiz/closed")
	public String quizClosed(Model model, @RequestParam("id") long id,
			@RequestParam(value = "mode", required=false, defaultValue="fromPolish") String mode,
			@RequestParam(value = "number") int number) {
		
		Dictionary dictionary = dictionaryRepository.findOne(id);
		List<Word> words = new ArrayList<Word>();
		words.addAll(dictionary.getWords());
		QuizMode qMode = QuizMode.valueOf(mode);
		List<Question> questions = QuestionABCD.getQuerySet(words,number,qMode);
		
		return openQuiz(model,questions,dictionary,QuizType.CLOSED,"quiz_abcd");
	}
	
	@RequestMapping("/quiz/open")
	public String quizOpen(Model model, @RequestParam("id") long id,
			@RequestParam(value = "mode", required=false, defaultValue="fromPolish") String mode,
			@RequestParam(value = "number") int number) {
		
		Dictionary dictionary = dictionaryRepository.findOne(id);
		List<Word> words = new ArrayList<Word>();
		words.addAll(dictionary.getWords());
		QuizMode qMode = QuizMode.valueOf(mode);
		List<Question> questions = Question.getQuerySet(words,number,qMode);
		
		return openQuiz(model,questions,dictionary,QuizType.OPEN,"quiz_open");
	}
	
	private String openQuiz(Model model, List<Question> questions, Dictionary dictionary,
			QuizType quizType, String quizTemplate) {
		
		
		model.addAttribute("questions", questions);
		model.addAttribute("questionsNumber", questions.size());
		model.addAttribute("dictionaryName", dictionary.getName());
		model.addAttribute("dictionaryCategory", dictionary.getCategory());
		model.addAttribute("dictionarySize", dictionary.getWords().size());
		model.addAttribute("dictionaryId", dictionary.getDictionaryId());
		boolean hasEnoughWords = questions.size() > 0 ? true : false;
		model.addAttribute("hasEnoughWords", hasEnoughWords);
		
		model.addAttribute("quizType", quizType.getName());
		
		return quizTemplate;
	}
}