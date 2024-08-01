package com.moat.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.moat.constant.ValidationMsg;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
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
    logger.debug("Constructing ErrorController!");

    this.resFact = resFact;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    logger.debug("In handleValidationException() in ErrorController.");

    // Note: Only return first validation error message
    ObjectError firstError = e.getBindingResult().getAllErrors().get(0);

    return resFact.build("message", firstError.getDefaultMessage(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleValidationExceptions(
      ConstraintViolationException e) {
    logger.debug("In handleValidationException() in ErrorController.");

    // Note: Only return first validation error message
    ConstraintViolation<?> violation =
        e.getConstraintViolations().iterator().next();

    return resFact.build("message", violation.getMessage(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    logger.debug(
        "In handleHttpMessageNotReadableException() in ErrorController.");

    Throwable cause = e.getCause();
    logger.info("Handling " + cause.getClass().getName() + " exception.");

    if (cause instanceof MismatchedInputException) {
      logger.debug("Handling instanceof MismatchedInputException.");

      return resFact.build("message", ValidationMsg.INCORRECT_DATA_TYPE,
          HttpStatus.BAD_REQUEST);
    }

    if (cause instanceof JsonMappingException) {
      logger.debug("Handling instanceof JsonMappingException.");

      return resFact.build("message", ValidationMsg.INCORRECT_DATA_TYPE,
          HttpStatus.BAD_REQUEST);
    }

    if (cause instanceof JsonParseException) {
      logger.debug("Handling instanceof JsonParseException.");

      return resFact.build("message", ValidationMsg.JSON_PARSE_ERROR,
          HttpStatus.BAD_REQUEST);
    }

    // TODO: Check subtypes in docs
    if (cause instanceof JsonProcessingException) {
      logger.debug("Handling instanceof JsonProcessingException.");

      return resFact.build("message", ValidationMsg.INCORRECT_DATA_TYPE,
          HttpStatus.BAD_REQUEST);
    }

    return resFact.build("message", ValidationMsg.ERROR_PROCESSING_REQUEST,
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<?> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    logger.debug(
        "In handleMethodArgumentTypeMismatchException() in ErrorController.");

    return resFact.build("message", ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE,
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Thrown when method annotation authentication fails.
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(
      AccessDeniedException e) {
    logger.debug("In handleAccessDeniedException() in ErrorController.");

    return resFact.build("message", ValidationMsg.ERROR_UNAUTHORISED,
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGenericException(Exception e) {
    logger.debug("In handleGenericException() in ErrorController.");

    // TODO: Remove in prod
    e.printStackTrace();

    return resFact.build("message", ValidationMsg.UNKNOWN_SERVER_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
