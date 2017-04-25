package io2017.users;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import io2017.validators.PasswordMatches;
import io2017.validators.ValidEmail;

@PasswordMatches
public class UserDto {
	
	@NotNull
    @NotEmpty(message="Pole nie może być puste")
    private String username;
	
	@NotNull
    @NotEmpty(message="Pole nie może być puste")
    private String name;
     
    @NotNull
    @NotEmpty(message="Pole nie może być puste")
    private String surname;
     
    @ValidEmail
    @NotNull
    @NotEmpty(message="Pole nie może być puste")
    private String email;
    
    @NotNull
    @NotEmpty(message="Pole nie może być puste")
    private String password;
    
    @NotNull
    @NotEmpty(message="Pole nie może być puste")
    private String matchingPassword;
    
    private boolean enabled = false;
    
    private boolean isAdmin = false;
    
    public UserDto() {
    	
    }
    
	public UserDto(String username, String name, String surname, String email, String password,
			String matchingPassword) {
		super();
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.matchingPassword = matchingPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
