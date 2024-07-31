package com.moat.validator.misc;

import com.moat.constant.Constants;
import com.moat.constant.ValidationMsg;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator
    implements ConstraintValidator<PasswordValid, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if (value == null) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.PASSWORD_NULL_MSG).addConstraintViolation();

      return false;
    }

    int length = value.trim().length();
    if (length < Constants.PASSWORD_MIN_LENGTH ||
        length > Constants.PASSWORD_MAX_LENGTH) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.PASSWORD_LENGTH_MSG).addConstraintViolation();

      return false;
    }

    Pattern pattern = Pattern.compile(Constants.PASSWORD_PATTERN);
    if (!pattern.matcher(value).find()) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.PASSWORD_PATTERN_MSG).addConstraintViolation();

      return false;
    }

    return true;
  }
}
