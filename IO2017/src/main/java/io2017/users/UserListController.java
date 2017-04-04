package io2017.users;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

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
        

        User me = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long myId= me.getUserId();

        model.addAttribute("myId", myId);
        
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
    	model.addAttribute("enabled", editUser.getEnabled());
    	model.addAttribute("roleAdmin", isAdmin);
    	
    	return "edit_user";
    }
    
    @RequestMapping("/admin/users/newUser/submit")
    public String saveUser( @ModelAttribute("user") User user, 
    						@RequestParam(value="enabled", required = false, defaultValue = "off") String enabled,
    						@RequestParam(value="roleAdmin", required = false, defaultValue = "off") String roleAdmin) {
    	
    	//TODO userValidator i reject values
    	user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

    	if(enabled.equals("on")) {
    		user.setEnabled(true);
    	} else {
    		user.setEnabled(false);
    	}
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
    							@RequestParam(value="enabled", required = false, defaultValue = "off") String enabled,
    							@RequestParam(value="roleAdmin", required = false, defaultValue = "off") String roleAdmin) {
    	
    	//TODO userValidator i reject values

    	if(newPassword != null && newPassword.equals("") == false) {
    		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
    	}
    	
    	System.out.println(roleAdmin);
    	
    	if(enabled.equals("on")) {
    		user.setEnabled(true);
    	} else {
    		user.setEnabled(false);
    	}
    	UserRole userRole = userRolesRepository.findOne(
    								userRolesRepository.findRoleIdByUserId(user.getUserId()).get(0));
  
    	if(roleAdmin.equals("on")) {
    		userRole.setRole("ROLE_ADMIN");
    	} else {
    		userRole.setRole("ROLE_USER");
    	}
    	userRolesRepository.save(userRole);
    	
    	userRepository.save(user);
    	System.out.println("saveEditedUser");
    	return "redirect:" + "/admin/users";
    }
    
    @RequestMapping("/admin/users/deleteUser")
    public String deleteUser(Model model, @RequestParam("id") long id) {
    	Long roleId = userRolesRepository.findRoleIdByUserId(id).get(0);
    	UserRole userRole = userRolesRepository.findOne(roleId);
    	userRolesRepository.delete(userRole);
    	userRepository.delete(id);
    	
    	return "redirect:" + "/admin/users";
    }
    
    @RequestMapping("/register/new")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        
        return "register_new";
    }
    
    @RequestMapping("/register/new/submit")
    public ModelAndView registerUserAccount(
    		
    	@ModelAttribute("userDto") @Valid UserDto accountDto, 
    	BindingResult result, 
    	WebRequest request, 
    	Errors errors) {

        User registered = new User();
        if (!result.hasErrors()) {
            registered = createUserAccount(accountDto, result);
        	registered.setPassword(new BCryptPasswordEncoder().encode(registered.getPassword()));
        	userRepository.save(registered);
        	userRolesRepository.save(new UserRole(null, registered.getUserId(), "ROLE_USER"));
        	
        	System.out.println("savedUser");
        }
    	
        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }
        
//    	return "home";

        if (result.hasErrors()) {
            return new ModelAndView("register_new", "userDto", accountDto);
        } 
        else {
            return new ModelAndView("login");
        }
    }
    
    private User createUserAccount(UserDto accountDto, BindingResult result) {
        try {
            User registered = registerNewUserAccount(accountDto);
            return registered;
        } catch (EmailExistsException e) {
            return null;
        }
    }
    
    public User registerNewUserAccount(UserDto accountDto) 
    	      throws EmailExistsException {
    	         
    	        if (emailExist(accountDto.getEmail())) {   
    	            throw new EmailExistsException(
    	              "Istnieje już konto dla tego adresu mailowego: " + accountDto.getEmail());
    	        }
    	        User user = new User();    
    	        user.setName(accountDto.getName());
    	        user.setUserName(accountDto.getUsername());
    	        user.setSurname(accountDto.getSurname());
    	        user.setPassword(accountDto.getPassword());
    	        user.setEmail(accountDto.getEmail());
    	        user.setEnabled(true);
    	        return user;
//    	        return repository.save(user);      //TODO: email authorization 
    	    }
    	    
    	    private boolean emailExist(String email) {
    	        User user = userRepository.findByEmail(email);
    	        if (user != null) {
    	            return true;
    	        }
    	        return false;
    	    }
}
