package io2017.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE,ElementType.FIELD, ElementType.ANNOTATION_TYPE}) 
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DictionaryNameValidator.class)
@Documented
public @interface ValidDictionaryName {   
    String message() default "Słownik o tej nazwie już istnieje";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
