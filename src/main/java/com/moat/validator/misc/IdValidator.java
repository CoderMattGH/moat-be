package com.moat.validator.misc;

import com.moat.constant.ValidationMsg;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdValidator implements ConstraintValidator<IdValid, Long> {
  @Override
  public boolean isValid(Long value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if (value == null) {
      context.buildConstraintViolationWithTemplate(ValidationMsg.ID_NULL_MSG)
          .addConstraintViolation();

      return false;
    }

    if (value <= 0) {
      context.buildConstraintViolationWithTemplate(ValidationMsg.ID_VALUE_MSG)
          .addConstraintViolation();

      return false;
    }

    return true;
  }
}
