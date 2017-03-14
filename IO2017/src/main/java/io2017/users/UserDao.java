package io2017.users;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao  extends CrudRepository<User, Long> {
	
	/*
	@Query("select a.userid, a.username from User a")
	public List<User> findAll();
	
	@Query("select a.userid, a.username from User a")
	public List<User> findAdmins();
	*/
	@Query("Select c from UserRole b, User c where b.role=?1 and b.userid=c.userId")
	public List<User> findUsersWithRole(String role);
}
