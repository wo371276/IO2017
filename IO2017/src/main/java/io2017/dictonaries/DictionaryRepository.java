package io2017.dictonaries;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io2017.users.User;

@Repository
public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {

	List<Dictionary> findByUser(User user);
}
