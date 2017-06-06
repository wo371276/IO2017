package io2017.dictionaries;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io2017.users.User;

@Repository
public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {

	List<Dictionary> findByUser(User user);
	
	Dictionary findByName(String name);
}
