package io2017.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io2017.users.dto.UserRegisterDto;

public class PasswordMatchesValidator 
implements ConstraintValidator<PasswordMatches, Object> { 
   
  @Override
  public void initialize(PasswordMatches constraintAnnotation) {       
  }
  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context){   
      UserRegisterDto user = (UserRegisterDto) obj;
      return user.getPassword().equals(user.getMatchingPassword());    
  }     
}
