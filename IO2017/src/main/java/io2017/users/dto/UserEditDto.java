package io2017.users.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io2017.users.User;

public class UserEditDto extends UserDto {
	
	private Long userId;
	
	private String password = "";
	
	public UserEditDto() {
		super();
	}
	
	public UserEditDto(String username, String name, String surname, String email,
						Long userId, String password) {
		super(username, name, surname, email);
		
		this.userId = userId;
		this.password = password;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public static UserEditDto buildDto(User user) {
		UserEditDto userEditDto = new UserEditDto(user.getUserName(), user.getName(), user.getSurname(),
											user.getEmail(), user.getUserId(), "");
		
		return userEditDto;
	}
	
	public void saveUser(User userBeforeEdit) {
		userBeforeEdit.setSurname(this.surname);
		userBeforeEdit.setUserName(this.username);
		userBeforeEdit.setName(this.name);
		userBeforeEdit.setEnabled(this.enabled);
		userBeforeEdit.setEmail(this.email);
		if(this.password != null && this.password.equals("") == false) {
			userBeforeEdit.setPassword(new BCryptPasswordEncoder().encode(this.password));
		}
		
	}
	
}
