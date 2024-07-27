package com.moat.controller;

import com.moat.dto.MessageDTO;
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
    logger.info("Constructing MainController.");

    this.endpointService = endpointService;
  }

  @GetMapping(value = "/", produces = "application/json")
  public ResponseEntity<String> getEndpoints() {
    logger.info("In getEndpoints() in MainController.");

    String endpoints;
    try {
      endpoints = endpointService.getEndpoints();
    } catch (Exception e) {
      return new ResponseEntity(new MessageDTO("Cannot fetch endpoints!"),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(endpoints, HttpStatus.OK);
  }
}
