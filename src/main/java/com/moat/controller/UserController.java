package com.moat.controller;

import com.moat.constant.ValidationMsg;
import com.moat.dto.UserDTO;
import com.moat.exception.AlreadyExistsException;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import com.moat.service.UserService;
import com.moat.validator.group.PatchUserDetailsGroup;
import com.moat.validator.misc.UsernameValid;
import com.moat.validator.group.SaveUserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.util.List;

@CrossOrigin
@RestController
@Validated
@RequestMapping("/user")
public class UserController {
  private final static Logger logger =
      LoggerFactory.getLogger(UserController.class);

  private final UserService userService;
  private final DynamicResponseWrapperFactory resFact;

  public UserController(UserService userService,
      DynamicResponseWrapperFactory resFact) {
    logger.debug("Constructing UserController.");

    this.userService = userService;
    this.resFact = resFact;
  }

  @GetMapping("/{username}/")
  @PreAuthorize(
      "#username == authentication.principal.username or hasRole('ADMIN')")
  public ResponseEntity<?> getUserByUsername(
      @PathVariable @UsernameValid String username) {
    logger.debug("In getUserByUsername() in UserController.");

    UserDTO user;
    try {
      user = userService.selectByUsername(username);
    } catch (NoResultException e) {
      return resFact.build("message", ValidationMsg.USER_DOES_NOT_EXIST,
          HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_GETTING_USER,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Remove sensitive fields
    user.setPassword(null);

    return resFact.build("user", user, HttpStatus.OK);
  }

  @GetMapping("/")
  public ResponseEntity<?> getUsers() {
    logger.debug("In getUsers() in UserController");

    List<UserDTO> users;
    try {
      users = userService.selectAll();
    } catch (NoResultException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_GETTING_USERS,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Remove sensitive fields
    users.forEach(user -> user.setPassword(null));

    return resFact.build("users", users, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<?> postUser(
      @RequestBody @Validated(SaveUserGroup.class) UserDTO user) {
    logger.debug("In postUser() in UserController.");

    UserDTO newUser;

    try {
      newUser = userService.create(user);
    } catch (AlreadyExistsException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_POSTING_USER,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Remove sensitive fields
    newUser.setPassword(null);

    return resFact.build("user", newUser, HttpStatus.OK);
  }

  @PatchMapping("/")
  @PreAuthorize("#user.id == authentication.principal.id or hasRole('ADMIN')")
  public ResponseEntity<?> patchUserDetails(
      @RequestBody @Validated(PatchUserDetailsGroup.class) UserDTO user) {
    logger.debug("In patchUserDetails() in UserController.");

    UserDTO updatedUser;
    try {
      updatedUser = userService.updateUserDetails(user);
    } catch (NoResultException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (AlreadyExistsException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_UPDATING_USER,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Remove sensitive fields
    updatedUser.setPassword(null);

    return resFact.build("user", updatedUser, HttpStatus.OK);
  }
}
