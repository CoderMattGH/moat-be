package com.moat.controller;

import com.moat.dto.MessageDTO;
import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;
import com.moat.exception.MOATValidationException;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
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
  private final static Logger logger =
      LoggerFactory.getLogger(UserController.class);

  private final UserService userService;
  private final DynamicResponseWrapperFactory resFact;

  public UserController(UserService userService,
      DynamicResponseWrapperFactory resFact) {
    logger.info("Constructing UserController.");

    this.userService = userService;
    this.resFact = resFact;
  }

  @GetMapping("/")
  public ResponseEntity<?> getUsers() {
    logger.info("In getUsers() in UserController");

    List<MOATUser> users = userService.selectAllUsers();

    if (users.isEmpty()) {
      return resFact.build("message", "No users found!", HttpStatus.NOT_FOUND);
    }

    return resFact.build("users", users, HttpStatus.OK);
  }

  @GetMapping("/{username}")
  public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
    logger.info("In getUserByUsername() in UserController.");

    MOATUser user;
    try {
      user = userService.selectByUsername(username);
    } catch (NoResultException e) {
      return resFact.build("message", "No user found!", HttpStatus.NOT_FOUND);
    }

    return resFact.build("user", user, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<?> postUser(@RequestBody @Valid MOATUser user) {
    logger.info("In postUser() in UserController.");

    try {
      userService.createUser(user);
    } catch (AlreadyExistsException | MOATValidationException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
