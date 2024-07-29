package com.moat.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.moat.constant.ValidationMsg;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorController {
  private final static Logger logger =
      LoggerFactory.getLogger(ErrorController.class);

  private final DynamicResponseWrapperFactory resFact;

  public ErrorController(DynamicResponseWrapperFactory resFact) {
    logger.info("Constructing ErrorController!");

    this.resFact = resFact;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    logger.info("In handleValidationException() in ErrorController.");

    // Note: Only return first validation error message
    ObjectError firstError = ex.getBindingResult().getAllErrors().get(0);

    return resFact.build("message", firstError.getDefaultMessage(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleValidationExceptions(
      ConstraintViolationException e) {
    logger.info("In handleValidationException() in ErrorController.");

    // Note: Only return first validation error message
    ConstraintViolation<?> violation =
        e.getConstraintViolations().iterator().next();

    return resFact.build("message", violation.getMessage(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<?> handleJsonParseException(JsonParseException e) {
    logger.info("In handleJsonParseException() in ErrorController.");

    return resFact.build("message", ValidationMsg.JSON_PARSE_ERROR,
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MismatchedInputException.class)
  public ResponseEntity<?> handleMismatchedInputException(
      MismatchedInputException e) {
    logger.info("In handleMisMatchedInputException() in ErrorController.");

    return resFact.build("message", ValidationMsg.INCORRECT_DATA_TYPE,
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<?> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    logger.info(
        "In handleMethodArgumentTypeMismatchException() in ErrorController.");

    return resFact.build("message", ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE,
        HttpStatus.BAD_REQUEST);
  }

  // TODO: Generic exception handler!
}
