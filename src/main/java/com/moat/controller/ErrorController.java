package com.moat.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorController {
  private final static Logger logger =
      LoggerFactory.getLogger(ErrorController.class);

  private final DynamicResponseWrapperFactory resFact;

  public ErrorController(DynamicResponseWrapperFactory resFact) {
    logger.info("Constructing ErrorController!");

    this.resFact = resFact;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    logger.info("In handleValidationException() in ErrorController.");

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return errors;
  }

  // TODO: More specific error message
  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<?> handleJsonParseException(JsonParseException e) {
    logger.info("In handleJsonParseException() in ErrorController.");

    return resFact.build("message",
        "One or more fields was the incorrect data type!",
        HttpStatus.BAD_REQUEST);
  }
}
