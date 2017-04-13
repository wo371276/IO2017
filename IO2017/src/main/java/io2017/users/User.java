package io2017.users;
import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io2017.dictonaries.Dictionary;

@Entity
@Table(name = "users")
public class User implements Serializable {

private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="userid")
	private Long userId;

	@Column(name = "username", unique = true)
	private String userName;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "surname")
	private String surname;
	
	@Column(name = "password")
	private String password;
	private String matchingPassword;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "enabled")
	Boolean enabled;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Dictionary> dictionaries;

	
	public User(){
	}
	
	public User(User user) {
        this.userId = user.userId;
        this.userName = user.userName;
        this.email = user.email;       
        this.password = user.password;
        this.enabled = user.enabled;
        //this.userRole = user.userRole;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public void seMatchingtPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(Set<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

}