package com.moat.validator.score;

import com.moat.constant.ValidationMsg;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ScoreValidator
    implements ConstraintValidator<ScoreValid, Integer> {
  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if (value == null) {
      context.buildConstraintViolationWithTemplate(ValidationMsg.SCORE_NULL_MSG)
          .addConstraintViolation();

      return false;
    }

    if (value < 0) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.SCORE_POSITIVE_INT_MSG).addConstraintViolation();

      return false;
    }

    return true;
  }
}
