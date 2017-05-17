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
	
	// TODO aktualizowanie wyniku użytkownika
	private void addScore(User user, int scoreVal, int mode, int dictionaryId) {
		List<Score> userScores = scoreRepository.findByUser(user);
		
		boolean shouldAdd = true;
		
		if (!userScores.isEmpty()) {
			for (Score score : userScores) {
				if (score.getMode() == mode) {
					shouldAdd = false;
					break;
				}
			}
		}
		
		Dictionary dictionary = dictionaryRepository.findOne(Long.valueOf(dictionaryId));
		
		if (shouldAdd) {
			Score newScore = new Score(scoreVal, mode, dictionary, user);
			scoreRepository.save(newScore);
			
		} else {
			
		}
		
	}
}
