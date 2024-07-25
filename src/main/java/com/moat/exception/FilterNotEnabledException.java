package com.moat.exception;

public class FilterNotEnabledException extends RuntimeException {
  public FilterNotEnabledException(String errorMessage) {
    super(errorMessage);
  }
}
