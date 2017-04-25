package io2017.dictionaries;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface WordRepository extends CrudRepository<Word, Long> {

	List<Word> findByDictionary(Dictionary dictionary);
}
