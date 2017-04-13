package io2017.dictonaries;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io2017.categories.Category;

@Repository
public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {

}
