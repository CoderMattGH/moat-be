package com.moat.validator.score;

import com.moat.constant.ValidationMsg;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MissesValidator
    implements ConstraintValidator<MissesValid, Integer> {
  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if (value == null) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.MISSES_NULL_MSG).addConstraintViolation();

      return false;
    }

    if (value < 0) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.MISSES_VALUE_MSG).addConstraintViolation();

      return false;
    }

    return true;
  }
}
