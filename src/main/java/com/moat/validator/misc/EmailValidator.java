package com.moat.validator.misc;

import com.moat.constant.Constants;
import com.moat.constant.ValidationMsg;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailValid, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if (value == null) {
      context.buildConstraintViolationWithTemplate(ValidationMsg.EMAIL_NULL_MSG)
          .addConstraintViolation();

      return false;
    }

    int length = value.trim().length();
    if (length < Constants.EMAIL_MIN_LENGTH ||
        length > Constants.EMAIL_MAX_LENGTH) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.EMAIL_LENGTH_MSG).addConstraintViolation();

      return false;
    }

    Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN);
    if (!pattern.matcher(value).find()) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.EMAIL_PATTERN_MSG).addConstraintViolation();

      return false;
    }

    return true;
  }
}
