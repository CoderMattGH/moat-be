package com.moat.controller;

import com.moat.dto.AuthenticationDTO;
import com.moat.jwt.JwtUtil;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Validated
@RequestMapping(value = "/authenticate")
public class AuthenticateController {
  private final static Logger logger =
      LoggerFactory.getLogger(AuthenticateController.class);

  private final DynamicResponseWrapperFactory resFact;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService moatAdminDetailsService;
  private final UserDetailsService moatUserDetailsService;
  private final JwtUtil jwtUtil;

  public AuthenticateController(DynamicResponseWrapperFactory resFact,
      AuthenticationManager authenticationManager,
      UserDetailsService moatAdminDetailsService,
      UserDetailsService moatUserDetailsService, JwtUtil jwtUtil) {
    logger.debug("Constructing LoginController.");

    this.resFact = resFact;
    this.authenticationManager = authenticationManager;
    this.moatAdminDetailsService = moatAdminDetailsService;
    this.moatUserDetailsService = moatUserDetailsService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody AuthenticationDTO authenticationDTO) throws Exception {
    logger.debug("In createAuthenticationToken() in AuthenticateController.");

    authenticate(authenticationDTO.getUsername(),
        authenticationDTO.getPassword(), authenticationDTO.getRole());

    System.out.println("Before generating token!");

    UserDetails details;

    if (authenticationDTO.getRole().equals("ADMIN")) {
      details = moatAdminDetailsService.loadUserByUsername(
          authenticationDTO.getUsername());
    } else if (authenticationDTO.getRole().equals("USER")) {
      details = moatUserDetailsService.loadUserByUsername(
          authenticationDTO.getUsername());
    } else {
      // TODO: Fix exception
      throw new Exception("ROLE NOT SPECIFIED!");
    }

    System.out.println("Generating TOKEN!");
    // TODO: Generate token with role as well as username.
    String token = jwtUtil.generateToken(details.getUsername());

    return resFact.build("token", token, HttpStatus.OK);
  }

  // TODO: Fix exceptions
  private void authenticate(String username, String password, String role)
      throws Exception {
    try {
      authenticationManager.authenticate(
          (new UsernamePasswordAuthenticationToken(username, password)));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}
