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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import io2017.exceptions.EmailExistsException;
import io2017.exceptions.EmptyPasswordException;
import io2017.exceptions.UserExistsException;
import io2017.helpers.QuizType;
import io2017.helpers.RegistrationHashHelper;
import io2017.scores.Score;
import io2017.scores.ScoreRepository;
import io2017.users.dto.UserEditDto;
import io2017.users.dto.UserRegisterDto;

@Controller
public class UserListController {
	private UserRepository userRepository;
	private UserRolesRepository userRolesRepository;
	private ScoreRepository scoreRepository;
	private UserDao userDao;
	/* example
	 * @RequestParam(value="name", required=false, defaultValue="World") String name, 
	 */
	@Autowired
	public UserListController(UserRepository userRepository, UserDao userDao,
						ScoreRepository scoreRepository,
						UserRolesRepository userRolesRepository) {
		this.userRepository = userRepository;
		this.userDao = userDao;
		this.scoreRepository = scoreRepository;
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
    	UserRegisterDto userDto = new UserRegisterDto();
    	model.addAttribute("user", userDto);
    	
    	return "create_user";
    }

    @RequestMapping("/admin/users/newUser/submit")
    public String saveUser( @ModelAttribute("user") UserRegisterDto userDto,
    						BindingResult result, 
    				    	WebRequest request, 
    				    	Errors errors) {
    	 User registered = new User();
         if (!result.hasErrors()) {
        	 try {
         			if(userDto.getPassword() == null || userDto.getPassword().equals("")) {
         				throw new EmptyPasswordException();
         			}
					registered = registerNewUserAccount(userDto);
					registered.setPassword(new BCryptPasswordEncoder().encode(registered.getPassword()));
					
					if(userDto.isEnabled()) {
						registered.setEnabled(true);
					} else {
						registered.setEnabled(false);
					}
					
					userRepository.save(registered);
					
					if(userDto.isAdmin()) {
			    		userRolesRepository.save(new UserRole(null, registered.getUserId(), "ROLE_ADMIN"));
			    	} else {
			    		userRolesRepository.save(new UserRole(null, registered.getUserId(), "ROLE_USER"));
			    	}

             	System.out.println("savedUser");
            } catch (EmailExistsException e) {
                result.rejectValue("email", "message.regError", "Istnieje już konto dla adresu mailowego <" + userDto.getEmail() + ">");
            } catch (UserExistsException e) {
                result.rejectValue("username", "message.regError", "Istnieje już użytkownik o loginie <" + userDto.getUsername() + ">");
 			} catch (EmptyPasswordException e) {
 				result.rejectValue("username", "message.regError", e.toString());
 			}
         	
         }
         
     	System.out.println(result.getGlobalErrorCount());

         if (result.hasErrors()) {
             return "create_user";
         } 
         else {
        	 return "redirect:" + "/admin/users";
         }
    }
     
    @RequestMapping("/admin/users/editUser")
    public String editUser(Model model, @RequestParam("id") long id) {
    	
    	User editUser = userRepository.findOne(id);
    	UserEditDto userEditDto = UserEditDto.buildDto(editUser);
    	
    	boolean isAdmin = userRolesRepository.findRoleByUserId(id).get(0).equals("ROLE_ADMIN");
    	userEditDto.setEnabled(editUser.getEnabled());
    	userEditDto.setAdmin(isAdmin);
    	
    	model.addAttribute("user", userEditDto);
    	
    	return "edit_user";
    }
    
    @RequestMapping("/users/editUser")
    public String editYourself(Model model) {
    	
    	User me = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEditDto userEditDto = UserEditDto.buildDto(me);
    	
    	boolean isAdmin = userRolesRepository.findRoleByUserId(me.getUserId()).get(0).equals("ROLE_ADMIN");
    	userEditDto.setEnabled(me.getEnabled());
    	userEditDto.setAdmin(isAdmin);
    	
    	model.addAttribute("user", userEditDto);
    	
    	return "edit_yourself";
    }
    
    @RequestMapping("/admin/users/editUser/submit")
    public String saveEditedUser(@ModelAttribute("user") @Valid UserEditDto editUser,
    							BindingResult result, 
        				    	WebRequest request, 
        				    	Errors errors)  {
    	
    	UserRole userRole = userRolesRepository.findOne(
    								userRolesRepository.findRoleIdByUserId(editUser.getUserId()).get(0));
    	
    	try {
    		User userBeforeEdit = userRepository.findOne(editUser.getUserId());
    		if(userBeforeEdit.getUserName().equals(editUser.getUsername()) == false)  {
    			//skoro nowa nazwa jest inna to trzeba sprawdzić
    			//czy nie istnieje już użytkownik o tej nazwie
    			if (userRepository.findByUserName(editUser.getUsername()) != null) {   
		            throw new UserExistsException();
		        }
    	        
    		}
    		
    		if(userBeforeEdit.getEmail().equals(editUser.getEmail())== false ) {
    			//przypadek co wyżej tylko, że dla emaila
    			if (userRepository.findByEmail(editUser.getEmail()) != null) {   
    	            throw new EmailExistsException();
    	        }
    		}
    		editUser.saveUser(userBeforeEdit);
    		
        	userRepository.save(userBeforeEdit);
        	
        	if(editUser.isAdmin()) {
        		userRole.setRole("ROLE_ADMIN");
        	} else {
        		userRole.setRole("ROLE_USER");
        	}
        	userRolesRepository.save(userRole);
        	
    	} catch (EmailExistsException e) {
            result.rejectValue("email", "message.regError", e.toString());
        } catch (UserExistsException e) {
            result.rejectValue("username", "message.regError", e.toString());
        }
    	
    	if (result.hasErrors()) {
            return "edit_user";
        } 
        else {
        	return "redirect:" + "/admin/users";
        }
    }
    
    @RequestMapping("/users/editUser/submit")
    public String saveEditedYourself(@ModelAttribute("user") @Valid UserEditDto editUser,
    							BindingResult result, 
        				    	WebRequest request, 
        				    	Errors errors)  {
    	
    	UserRole userRole = userRolesRepository.findOne(
    								userRolesRepository.findRoleIdByUserId(editUser.getUserId()).get(0));
    	
    	try {
    		User userBeforeEdit = userRepository.findOne(editUser.getUserId());
    		if(userBeforeEdit.getUserName().equals(editUser.getUsername()) == false)  {
    			//skoro nowa nazwa jest inna to trzeba sprawdzić
    			//czy nie istnieje już użytkownik o tej nazwie
    			if (userRepository.findByUserName(editUser.getUsername()) != null) {   
		            throw new UserExistsException();
		        }
    	        
    		}
    		
    		if(userBeforeEdit.getEmail().equals(editUser.getEmail())== false ) {
    			//przypadek co wyżej tylko, że dla emaila
    			if (userRepository.findByEmail(editUser.getEmail()) != null) {   
    	            throw new EmailExistsException();
    	        }
    		}
    		editUser.saveUser(userBeforeEdit);
    		
        	userRepository.save(userBeforeEdit);
        	
        	if(editUser.isAdmin()) {
        		userRole.setRole("ROLE_ADMIN");
        	} else {
        		userRole.setRole("ROLE_USER");
        	}
        	userRolesRepository.save(userRole);
        	
    	} catch (EmailExistsException e) {
            result.rejectValue("email", "message.regError", e.toString());
        } catch (UserExistsException e) {
            result.rejectValue("username", "message.regError", e.toString());
        }
    	
    	if (result.hasErrors()) {
            return "edit_yourself";
        } 
        else {
        	return "redirect:" + "/profil?login=" + editUser.getUsername();
        }
    }
    
    @RequestMapping("/admin/users/deleteUser")
    public String deleteUser(Model model, @RequestParam("id") long id) {
    	Long roleId = userRolesRepository.findRoleIdByUserId(id).get(0);
    	UserRole userRole = userRolesRepository.findOne(roleId);
    	userRolesRepository.delete(userRole);
    	userRepository.delete(id);
    	
    	return "redirect:" + "/admin/users";
    }
    
    @RequestMapping("/users/deleteUser")
    public String deleteUser(Model model) {
    	User me = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	Long roleId = userRolesRepository.findRoleIdByUserId(me.getUserId()).get(0);
    	UserRole userRole = userRolesRepository.findOne(roleId);
    	userRolesRepository.delete(userRole);
    	userRepository.delete(me.getUserId());
    	
    	return "redirect:" + "/login?logout";
    }
    
    @RequestMapping("/register/new")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserRegisterDto userDto = new UserRegisterDto();
        model.addAttribute("userDto", userDto);
        
        return "register_new";
    }
    
    @RequestMapping("/register/new/submit")
    public ModelAndView registerUserAccount(
    		
    	@ModelAttribute("userDto") @Valid UserRegisterDto accountDto, 
    	BindingResult result, 
    	WebRequest request, 
    	Errors errors) {

        User registered = new User();
        if (!result.hasErrors()) {
        	try {
                registered = registerNewUserAccount(accountDto);
                registered.setPassword(new BCryptPasswordEncoder().encode(registered.getPassword()));
            	userRepository.save(registered);
            	userRolesRepository.save(new UserRole(null, registered.getUserId(), "ROLE_USER"));
            	
            	System.out.println("savedUser");
            } catch (EmailExistsException e) {
                result.rejectValue("email", "message.regError", "Istnieje już konto dla adresu mailowego <" + accountDto.getEmail() + ">");
            } catch (UserExistsException e) {
                result.rejectValue("username", "message.regError", "Istnieje już użytkownik o loginie <" + accountDto.getUsername() + ">");
			}
        	
        }
        
    	System.out.println(result.getGlobalErrorCount());

        if (result.hasErrors()) {
            return new ModelAndView("register_new", "userDto", accountDto);
        } 
        else {
            return new ModelAndView("login");
        }
    }
    
    private User registerNewUserAccount(UserRegisterDto accountDto) 
    	      throws EmailExistsException, UserExistsException {
		    	if (userRepository.findByUserName(accountDto.getUsername()) != null) {   
		            throw new UserExistsException();
		        }
    	        if (userRepository.findByEmail(accountDto.getEmail()) != null) {   
    	            throw new EmailExistsException();
    	        }
    	        User user = new User();    
    	        user.setName(accountDto.getName());
    	        user.setUserName(accountDto.getUsername());
    	        user.setSurname(accountDto.getSurname());
    	        user.setPassword(accountDto.getPassword());
    	        user.setEmail(accountDto.getEmail());
    	        user.setEnabled(true); //TODO: authorization
    	        return user;
    	    }
//confirmRegistration?code=
    @RequestMapping("/confirmRegistration")
    public String confirmRegistration(Model model, @RequestParam("code") String code) {
    	RegistrationHashHelper hashHelper = new RegistrationHashHelper(code);
    	if(hashHelper.isCorrectPattern()) {
    		User user = (User) userRepository.findOne(hashHelper.getId());
    		if(user != null) {
    			String email = user.getEmail();
    			try {
    				String correctHash = hashHelper.makeSHA1Hash(email);
        			if(correctHash.equals(hashHelper.getHashedEmail())) {
        				user.setEnabled(true);
        				userRepository.save(user);
        				model.addAttribute("message", "Aktywacja się powiodła");
        			} else {
        				model.addAttribute("message", "Aktywacja się nie powiodła. Błedny link.");
        			}
    			} catch (Exception e) {
    				e.getMessage();
    			}
    			
    		} else {
    			model.addAttribute("message", "Aktywacja się nie powiodła. Brak takiego użytkowniak.");
    		}
    	} else {
    		model.addAttribute("message", "Aktywacja się nie powiodła. Błedny link.");
    	}
    	
    	return "confirm_registration";
    }
    
    @RequestMapping("/searchUsers")
    public String searchUsers(Model model, @RequestParam("name") String name){
    	List<User> res = userRepository.findByUserNameContaining(name);
    	model.addAttribute("searchUsers", res);
    	model.addAttribute("name", name);
    	return "searchUsers";
    }
    
    @RequestMapping("/profil")
    public String showProfile(Model model, @RequestParam("login") String username) {
    	User user = userRepository.findByUserName(username);
    	List<Score> scores = scoreRepository.findByUser(user);
    	User me = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long myId= me.getUserId();

        model.addAttribute("myId", myId);
    	model.addAttribute("user", user);
    	model.addAttribute("scores", scores);
    	model.addAttribute("userPoints", this.calculateUserScore(scores));
    	
    	return "profile";
    }
    
    private int calculateUserScore(List<Score> scores) {
    	int scoreVal = 0;
    	
    	for (Score score : scores) {
    		int difficulty = score.getDictionary().getDifficulty();
    		
    		if (score.getQuizType().equals(QuizType.OPEN.getName())) {
    			scoreVal = Math.max(scoreVal, score.getScore() * difficulty * 2);
    		} else {
    			scoreVal = Math.max(scoreVal, score.getScore() * difficulty);
    		}
    		
    	}
    	
    	return scoreVal;
    }
}
