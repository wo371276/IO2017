package io2017.users;
import java.io.Serializable;
import javax.persistence.*;

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
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "enabled")
	Boolean enabled;
	
	
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


}