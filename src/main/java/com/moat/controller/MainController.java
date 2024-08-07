package com.moat.controller;

import com.moat.service.EndpointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class MainController {
  private final static Logger logger =
      LoggerFactory.getLogger(MainController.class);

  private final EndpointService endpointService;

  public MainController(EndpointService endpointService) {
    logger.debug("Constructing MainController.");

    this.endpointService = endpointService;
  }

  @GetMapping(value = "/", produces = "application/json")
  public ResponseEntity<?> getEndpoints() {
    logger.debug("In getEndpoints() in MainController.");

    String endpoints = endpointService.getEndpoints();

    return ResponseEntity.status(HttpStatus.OK).body(endpoints);
  }
}
