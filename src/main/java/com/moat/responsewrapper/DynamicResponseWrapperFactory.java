package com.moat.responsewrapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface DynamicResponseWrapperFactory {
  ResponseEntity<?> build(HttpStatus statusCode);

  ResponseEntity<?> build(String key, Object response, HttpStatus statusCode);
}
