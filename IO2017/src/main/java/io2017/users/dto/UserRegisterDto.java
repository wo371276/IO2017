package io2017.users.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import io2017.validators.PasswordMatches;

@PasswordMatches
public class UserRegisterDto extends UserDto {
	
    
    @NotNull
    @NotEmpty(message="Pole nie może być puste")
    private String password;
    
    @NotNull
    @NotEmpty(message="Pole nie może być puste")
    private String matchingPassword;
	
    public UserRegisterDto() {
    	super();
    }
    
	public UserRegisterDto(String username, String name, String surname, String email, String password,
			String matchingPassword) {
		super(username, name, surname, email);
		
		this.password = password;
		this.matchingPassword = matchingPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

}
