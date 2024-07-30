package com.moat.validator.user;

import com.moat.constant.ValidationMsg;
import com.moat.entity.MOATUser;

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
    if (length < MOATUser.EMAIL_MIN_LENGTH ||
        length > MOATUser.EMAIL_MAX_LENGTH) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.EMAIL_LENGTH_MSG).addConstraintViolation();

      return false;
    }

    Pattern pattern = Pattern.compile(MOATUser.EMAIL_PATTERN);
    if (!pattern.matcher(value).find()) {
      context.buildConstraintViolationWithTemplate(
          ValidationMsg.EMAIL_PATTERN_MSG).addConstraintViolation();

      return false;
    }

    return true;
  }
}
