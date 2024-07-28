package com.moat.controller;

import com.moat.responsewrapper.DynamicResponseWrapperFactory;
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
  private final DynamicResponseWrapperFactory resFact;

  public MainController(EndpointService endpointService,
      DynamicResponseWrapperFactory resFact) {
    logger.info("Constructing MainController.");

    this.endpointService = endpointService;
    this.resFact = resFact;
  }

  @GetMapping(value = "/", produces = "application/json")
  public ResponseEntity<?> getEndpoints() {
    logger.info("In getEndpoints() in MainController.");

    String endpoints;
    try {
      endpoints = endpointService.getEndpoints();
    } catch (Exception e) {
      return resFact.build("message", "Cannot fetch endpoints!",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.status(HttpStatus.OK).body(endpoints);
  }
}
