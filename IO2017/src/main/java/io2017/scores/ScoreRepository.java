package io2017.scores;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io2017.dictionaries.Dictionary;
import io2017.users.User;

public interface ScoreRepository extends CrudRepository<Score, Integer> {

	List<Score> findByDictionary(Dictionary dictionary);
	List<Score> findByUser(User user);
}
