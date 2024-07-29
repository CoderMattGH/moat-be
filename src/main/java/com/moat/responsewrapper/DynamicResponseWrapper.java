package com.moat.responsewrapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicResponseWrapper<T> {
  private Map<String, Object> responseMap = new HashMap<>();
  private HttpStatus statusCode;

  public DynamicResponseWrapper(HttpStatus statusCode) {
    this.statusCode = statusCode;
  }

  public DynamicResponseWrapper(String key, T response) {
    this.responseMap.put(key, response);
  }

  public DynamicResponseWrapper(String key, T response, HttpStatus statusCode) {
    this.responseMap.put(key, response);
    this.statusCode = statusCode;
  }

  public DynamicResponseWrapper(String key, List<T> response,
      HttpStatus statusCode) {
    this.responseMap.put(key, response);
    this.statusCode = statusCode;
  }

  public Map<String, Object> getResponseMap() {
    return responseMap;
  }

  public void addResponseMap(String key, Object response) {
    this.responseMap.put(key, response);
  }

  public void setResponseMap(Map<String, Object> responseMap) {
    this.responseMap = responseMap;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(HttpStatus statusCode) {
    this.statusCode = statusCode;
  }

  public ResponseEntity<?> getResponseEntity() {
    if (responseMap.isEmpty()) {
      return ResponseEntity.status(statusCode).build();
    }

    return ResponseEntity.status(statusCode).body(responseMap);
  }
}
