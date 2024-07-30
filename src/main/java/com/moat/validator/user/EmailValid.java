package com.moat.validator.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailValid {
  String message() default "Email is not valid!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
