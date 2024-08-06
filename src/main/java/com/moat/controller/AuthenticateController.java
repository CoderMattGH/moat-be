package com.moat.controller;

import com.moat.constant.ValidationMsg;
import com.moat.dto.UserDTO;
import com.moat.jwt.JwtUtil;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import com.moat.security.SecMOATUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/authenticate")
public class AuthenticateController {
  private final static Logger logger =
      LoggerFactory.getLogger(AuthenticateController.class);

  private final DynamicResponseWrapperFactory resFact;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService moatUserDetailsService;
  private final JwtUtil jwtUtil;

  public AuthenticateController(DynamicResponseWrapperFactory resFact,
      AuthenticationManager authenticationManager,
      UserDetailsService moatUserDetailsService, JwtUtil jwtUtil) {
    logger.debug("Constructing LoginController.");

    this.resFact = resFact;
    this.authenticationManager = authenticationManager;
    this.moatUserDetailsService = moatUserDetailsService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody UserDTO userDTO) {
    logger.debug("In createAuthenticationToken() in AuthenticateController.");

    try {
      authenticate(userDTO.getUsername(), userDTO.getPassword());
    } catch (BadCredentialsException e) {
      return resFact.build("message", ValidationMsg.INVALID_CREDENTIALS,
          HttpStatus.UNAUTHORIZED);
    } catch (DisabledException e) {
      return resFact.build("message", ValidationMsg.DISABLED_EXCEPTION,
          HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_LOGGING_IN,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    UserDetails details =
        moatUserDetailsService.loadUserByUsername(userDTO.getUsername());

    String token = jwtUtil.generateToken(details.getUsername());

    Map<String, Object> response = new HashMap<>();
    response.put("id", ((SecMOATUserDetails) details).getId());
    response.put("username", details.getUsername());
    response.put("token", token);

    boolean isAdmin = details.getAuthorities()
        .stream()
        .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

    if (isAdmin) {
      response.put("role", "ADMIN");
    } else {
      response.put("role", "USER");
    }

    return resFact.build("user", response, HttpStatus.OK);
  }

  private void authenticate(String username, String password)
      throws DisabledException, BadCredentialsException {
    authenticationManager.authenticate(
        (new UsernamePasswordAuthenticationToken(username, password)));
  }
}
