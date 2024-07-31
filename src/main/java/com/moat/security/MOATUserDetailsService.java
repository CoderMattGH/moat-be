package com.moat.security;

import com.moat.entity.Administrator;
import com.moat.service.AdministratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.NoResultException;

// TODO: Fix
public class MOATUserDetailsService implements UserDetailsService {
  private final static Logger logger =
      LoggerFactory.getLogger(MOATUserDetailsService.class);

  private final AdministratorService administratorService;

  public MOATUserDetailsService(AdministratorService administratorService) {
    logger.debug("Constructing MOATUserDetailsService.");

    this.administratorService = administratorService;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    logger.debug("In loadUserByUsername() in MOATUserDetailsService.");

    try {
      final Administrator administrator =
          administratorService.selectByUsername(username);

      String password = administrator.getPassword();

      return User.withUsername(username)
          .password(password)
          .roles("ADMIN", "USER")
          .build();
    } catch (NoResultException e) {
      logger.error("Administrator username not found!");
    } catch (Exception e) {
      logger.error("An unknown error occurred trying to login!");
      e.printStackTrace();
    }

    throw new UsernameNotFoundException(username);
  }
}