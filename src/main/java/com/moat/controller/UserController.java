package com.moat.controller;

import com.moat.dto.UserDTO;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import com.moat.service.UserService;
import com.moat.validator.group.PatchUserDetailsGroup;
import com.moat.validator.group.PatchUserPasswordGroup;
import com.moat.validator.misc.UsernameValid;
import com.moat.validator.group.SaveUserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    UserDTO user = userService.selectByUsername(username);

    // Remove sensitive fields
    user.setPassword(null);

    return resFact.build("user", user, HttpStatus.OK);
  }

  @GetMapping("/")
  public ResponseEntity<?> getUsers() {
    logger.debug("In getUsers() in UserController");

    List<UserDTO> users = userService.selectAll();

    // Remove sensitive fields
    users.forEach(user -> user.setPassword(null));

    return resFact.build("users", users, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<?> postUser(
      @RequestBody @Validated(SaveUserGroup.class) UserDTO user) {
    logger.debug("In postUser() in UserController.");

    UserDTO newUser = userService.create(user);

    // Remove sensitive fields
    newUser.setPassword(null);

    return resFact.build("user", newUser, HttpStatus.OK);
  }

  @PatchMapping("/")
  @PreAuthorize("#user.id == authentication.principal.id or hasRole('ADMIN')")
  public ResponseEntity<?> patchUserDetails(
      @RequestBody @Validated(PatchUserDetailsGroup.class) UserDTO user) {
    logger.debug("In patchUserDetails() in UserController.");

    UserDTO updatedUser = userService.updateUserDetails(user);

    // Remove sensitive fields
    updatedUser.setPassword(null);

    return resFact.build("user", updatedUser, HttpStatus.OK);
  }

  @PatchMapping("/password/")
  @PreAuthorize("#user.id == authentication.principal.id or hasRole('ADMIN')")
  public ResponseEntity<?> patchUserPassword(
      @RequestBody @Validated(PatchUserPasswordGroup.class) UserDTO user) {
    logger.debug("In patchUserPassword() in UserController.");

    UserDTO updatedUser = userService.updateUserPassword(user);

    // Remove sensitive fields
    updatedUser.setPassword(null);

    return resFact.build("user", updatedUser, HttpStatus.OK);
  }
}
