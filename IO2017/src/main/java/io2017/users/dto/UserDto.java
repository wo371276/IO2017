package io2017.users.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import io2017.validators.PasswordMatches;
import io2017.validators.ValidEmail;

public class UserDto {
	
	@NotNull
    @NotEmpty(message="Pole nie może być puste")
    protected String username;
	
	@NotNull
    @NotEmpty(message="Pole nie może być puste")
	protected String name;
     
    @NotNull
    @NotEmpty(message="Pole nie może być puste")
    protected String surname;
     
    @ValidEmail
    @NotNull
    @NotEmpty(message="Pole nie może być puste")
    protected String email;
    
    protected boolean enabled = false;
    
    protected boolean admin = false;
    
    public UserDto() {
    	
    }
    
	public UserDto(String username, String name, String surname, String email) {
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.email = email;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
