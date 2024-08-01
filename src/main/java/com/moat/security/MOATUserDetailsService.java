package com.moat.security;

import com.moat.constant.ValidationMsg;
import com.moat.dto.UserDTO;
import com.moat.entity.MOATUser;
import com.moat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service("moatUserDetailsService")
public class MOATUserDetailsService implements UserDetailsService {
  private final static Logger logger =
      LoggerFactory.getLogger(MOATUserDetailsService.class);

  private final UserService userService;

  public MOATUserDetailsService(UserService userService) {
    logger.debug("Constructing MOATUserDetailsService.");

    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    logger.debug("In loadUserByUsername() in MOATUserDetailsService.");

    final UserDTO user;

    try {
      user = userService.selectByUsername(username);
    } catch (NoResultException e) {
      throw new UsernameNotFoundException(ValidationMsg.USER_DOES_NOT_EXIST);
    }

    MOATUser loggedUser = new MOATUser();
    loggedUser.setId(user.getId());
    loggedUser.setUsername(user.getUsername());
    loggedUser.setPassword(user.getPassword());
    loggedUser.setEmail(user.getEmail());
    loggedUser.setBanned(user.isBanned());
    loggedUser.setVerified(user.isVerified());

    return new SecMOATUserDetails(loggedUser);
  }
}