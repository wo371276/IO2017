package io2017.users;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends CrudRepository<UserRole, Long> {

	@Query("select a.role from UserRole a, User b where b.userName=?1 and a.userid=b.userId")
	public List<String> findRoleByUserName(String username);
	
	@Query("select a.role from UserRole a where a.userid=?1")
	public List<String> findRoleByUserId(long userid);
	
	@Query("select a.userroleid from UserRole a where a.userid=?1")
	public List<Long> findRoleIdByUserId(long userid);
}