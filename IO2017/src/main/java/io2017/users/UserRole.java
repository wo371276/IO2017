package io2017.users;

import javax.persistence.*;

@Entity
@Table(name="user_roles")
public class UserRole {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_role_id")
	private Long userroleid;
	
	@Column(name="userid")
	private Long userid;
	
	@Column(name="role")
	private String role;

	public UserRole(Long userroleid, Long userid, String role) {
		this.userroleid = userroleid;
		this.userid = userid;
		this.role = role;
	}
	
	public UserRole() {
		
	}
	
	public boolean isAdmin() {
		if(role.equals("ROLE_ADMIN")) {
			return true;
		}
		return false;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
