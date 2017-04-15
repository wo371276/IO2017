package io2017.categories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CategoriesRepository extends CrudRepository<Category, Long> {
	public Category findByName(String name);
	
}