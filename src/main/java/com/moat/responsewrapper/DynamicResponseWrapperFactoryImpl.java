package com.moat.responsewrapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DynamicResponseWrapperFactoryImpl
    implements DynamicResponseWrapperFactory {
  public ResponseEntity<?> build(HttpStatus statusCode) {
    return new DynamicResponseWrapper<>(statusCode).getResponseEntity();
  }

  public ResponseEntity<?> build(String key, Object response,
      HttpStatus statusCode) {
    return new DynamicResponseWrapper<>(key, response,
        statusCode).getResponseEntity();
  }
}
