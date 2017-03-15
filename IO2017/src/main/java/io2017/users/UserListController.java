package io2017.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserListController {
	private UserRepository userRepository;
	private UserRolesRepository userRolesRepository;
	private UserDao userDao;
	/* example
	 * @RequestParam(value="name", required=false, defaultValue="World") String name, 
	 */
	@Autowired
	public UserListController(UserRepository userRepository, UserDao userDao,
						UserRolesRepository userRolesRepository) {
		this.userRepository = userRepository;
		this.userDao = userDao;
		this.userRolesRepository = userRolesRepository;
	}
	
    @RequestMapping("/admin/users")
    public String listUsers(Model model) {
        //Iterable<User> allUsersList = userRepository.findAll();
    	List<User> users = userDao.findUsersWithRole("ROLE_USER");
    	List<User> admins = userDao.findUsersWithRole("ROLE_ADMIN");
        System.out.println("test");
        model.addAttribute("users", users);
        model.addAttribute("admins", admins);
        
        return "admin_users";
    }
    
    @RequestMapping("/admin/users/newUser")
    public String createUser(Model model) {
    	User user = new User();
    	model.addAttribute("user", user);
    	model.addAttribute("roleAdmin", false);
    	
    	return "create_user";
    }
    
    @RequestMapping("/admin/users/editUser")
    public String editUser(Model model, @RequestParam("id") long id) {
    	
    	User editUser = userRepository.findOne(id);
    	model.addAttribute("user", editUser);
    	//System.out.println("id: " + id);
    	boolean isAdmin = userRolesRepository.findRoleByUserId(id).get(0).equals("ROLE_ADMIN");
    	model.addAttribute("roleAdmin", isAdmin);
    	
    	return "edit_user";
    }
    
    @RequestMapping("/admin/users/newUser/submit")
    public String saveUser(@ModelAttribute("user") User user, 
    						@RequestParam(value="roleAdmin", required = false, defaultValue = "off") String roleAdmin) {
    	
    	//TODO userValidator i rejest values
    	user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    	userRepository.save(user);
    	System.out.println(roleAdmin);
    	if(roleAdmin.equals("on")) {
    		userRolesRepository.save(new UserRole(null, user.getUserId(), "ROLE_ADMIN"));
    	} else {
    		userRolesRepository.save(new UserRole(null, user.getUserId(), "ROLE_USER"));
    	}
    	
    	System.out.println("savedUser");
    	
    	return "redirect:" + "/admin/users";
    }
    
    @RequestMapping("/admin/users/editUser/submit")
    public String saveEditedUser(@ModelAttribute("user") User user, 
    						@RequestParam(value="newPassword", required = false) String newPassword,
    						@RequestParam(value="roleAdmin", required = false, defaultValue = "off") String roleAdmin) {
    	
    	//TODO userValidator i rejest values


    	
    	
    	
    	if(newPassword != null) {
    		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
    	}
    	
    	System.out.println(roleAdmin);
    	
    	if(roleAdmin.equals("on")) {
    		userRolesRepository.save(new UserRole(null, user.getUserId(), "ROLE_ADMIN"));
    	} else {
    		userRolesRepository.save(new UserRole(null, user.getUserId(), "ROLE_USER"));
    	}
    	
    	userRepository.save(user);
    	System.out.println("saveEditedUser");
    	return "redirect:" + "/admin/users";
    }

}
