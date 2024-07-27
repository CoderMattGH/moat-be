package com.moat.controller;

import com.moat.dto.MessageDTO;
import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;
import com.moat.exception.MOATValidationException;
import com.moat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
  private static Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  public UserController(UserService userService) {
    logger.info("Constructing UserController.");

    this.userService = userService;
  }

  @GetMapping("/")
  public ResponseEntity<?> getUsers() {
    logger.info("In getUsers() in UserController");

    List<MOATUser> users = userService.selectAllUsers();

    if (users.isEmpty()) {
      return new ResponseEntity<>(new MessageDTO("No users found!"),
          HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{username}")
  public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
    logger.info("In getUserByUsername() in UserController.");

    MOATUser user;
    try {
      user = userService.selectByUsername(username);
    } catch (NoResultException e) {
      return new ResponseEntity<>(new MessageDTO("User not found!"),
          HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<?> postUser(@RequestBody @Valid MOATUser user) {
    logger.info("In postUser() in UserController.");

    try {
      userService.createUser(user);
    } catch (AlreadyExistsException e) {
      return new ResponseEntity<>(new MessageDTO(e.getMessage()),
          HttpStatus.BAD_REQUEST);
    } catch (MOATValidationException e) {
      return new ResponseEntity<>(new MessageDTO(e.getMessage()),
          HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(null, HttpStatus.CREATED);
  }
}
