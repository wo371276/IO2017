package io2017.categories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io2017.users.User;

@Repository
public interface CategoriesRepository extends CrudRepository<Category, Long> {
	public Category findByName(String name);
	
}