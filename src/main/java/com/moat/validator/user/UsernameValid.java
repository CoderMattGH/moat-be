package com.moat.validator.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameValid {
  String message() default "Username is not valid!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
