package io2017.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io2017.dictionaries.Dictionary;
import io2017.dictionaries.DictionaryRepository;
import io2017.scores.Score;
import io2017.scores.ScoreRepository;
import io2017.users.User;
import io2017.users.UserRepository;

@Controller
public class ApiController {
	
	DictionaryRepository dictionaryRepository;
	UserRepository userRepository;
	ScoreRepository scoreRepository;
	
	@Autowired
	public ApiController(DictionaryRepository dictionaryRepository, 
						UserRepository userRepository,
						ScoreRepository scoreRepository) {
		this.dictionaryRepository = dictionaryRepository;
		this.userRepository = userRepository;
		this.scoreRepository = scoreRepository;
		
	}
	

	@RequestMapping(value = "/api/submitScore", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String Submit(@RequestParam("score") String score,
			@RequestParam("modeType") String mode,
			@RequestParam("dictionaryId") String dictionaryId) {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// Long userId = user.getUserId();
		
		addScore(user, Integer.parseInt(score), Integer.parseInt(mode), Integer.parseInt(dictionaryId));
		
	    return "";
	}
	
	private void addScore(User user, int scoreVal, int mode, int dictionaryId) {
		List<Score> userScores = scoreRepository.findByUser(user);
		Dictionary dictionary = dictionaryRepository.findOne(Long.valueOf(dictionaryId));
		
		if (!userHasScore(user, scoreVal, mode, dictionaryId)) {
			Score score = new Score(scoreVal, mode, dictionary, user);
			scoreRepository.save(score);
			return;
		}
		
		// user ma już wynik, sprawdzamy czy trzeba zaktualizować
		for (Score score : userScores) {
			if (score.getMode() == mode && score.getDictionary().getDictionaryId() == dictionaryId) {
				score.setScore(Math.max(scoreVal, score.getScore()));
				scoreRepository.save(score);
				return;
			}
		}
		
	}
	
	private boolean userHasScore(User user, int scoreVal, int mode, int dictionaryId) {
		List<Score> userScores = scoreRepository.findByUser(user);
		
		if (userScores.isEmpty()) {
			return false;
		}
		
		for (Score score : userScores) {
			if (score.getMode() == mode && score.getDictionary().getDictionaryId() == dictionaryId) {
				return true;
			}
		}
		
		return false;
	}
}
