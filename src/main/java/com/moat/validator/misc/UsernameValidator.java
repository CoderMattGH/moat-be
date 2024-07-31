package com.moat.validator.misc;

import com.moat.constant.Constants;
import com.moat.constant.ValidationMsg;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UsernameValidator
    implements ConstraintValidator<UsernameValid, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if (value == null) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.USERNAME_NULL_MSG).addConstraintViolation();

      return false;
    }

    int length = value.trim().length();
    if (length < Constants.USERNAME_MIN_LENGTH ||
        length > Constants.USERNAME_MAX_LENGTH) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.USERNAME_LENGTH_MSG).addConstraintViolation();

      return false;
    }

    Pattern pattern = Pattern.compile(Constants.USERNAME_PATTERN);
    if (!pattern.matcher(value).find()) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.USERNAME_PATTERN_MSG).addConstraintViolation();

      return false;
    }

    return true;
  }
}
