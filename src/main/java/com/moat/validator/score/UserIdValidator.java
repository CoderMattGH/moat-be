package com.moat.validator.score;

import com.moat.constant.ValidationMsg;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserIdValidator implements ConstraintValidator<UserIdValid, Long> {
  @Override
  public boolean isValid(Long value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if (value == null) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.USER_ID_NULL_MSG).addConstraintViolation();

      return false;
    }

    if (value <= 0) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.USER_ID_VALUE_MSG).addConstraintViolation();

      return false;
    }

    return true;
  }
}

