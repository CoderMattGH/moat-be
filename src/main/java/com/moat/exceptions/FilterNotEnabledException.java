package com.moat.exceptions;

public class FilterNotEnabledException extends RuntimeException {
  public FilterNotEnabledException(String errorMessage) {
    super(errorMessage);
  }
}
