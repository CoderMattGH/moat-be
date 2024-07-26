package com.moat.controller;

import com.moat.dto.UserDTO;
import com.moat.entity.MOATUser;
import com.moat.service.UserService;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
  private static Logger logger = LoggerFactory.getLogger(UserController.class);

  private UserService userService;

  public UserController(UserService userService) {
    logger.info("Constructing UserController.");

    this.userService = userService;
  }

  @PostMapping("/")
  public ResponseEntity<MOATUser> postUser(@RequestBody UserDTO user) {
    throw new NotYetImplementedException();
  }

  @GetMapping("/")
  public ResponseEntity<List<MOATUser>> getUsers() {
    logger.info("In getUsers() in UserController");

    List<MOATUser> users = userService.selectAllUsers();

    if (users.isEmpty()) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{userid}")
  public ResponseEntity<MOATUser> getUserByUserId(@PathVariable String userid) {
    throw new NotYetImplementedException();
  }
}
