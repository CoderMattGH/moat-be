package com.moat.security;

import com.moat.dto.AdminDTO;
import com.moat.service.AdminService;
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

  private final AdminService adminService;

  public MOATUserDetailsService(AdminService adminService) {
    logger.debug("Constructing MOATUserDetailsService.");

    this.adminService = adminService;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    logger.debug("In loadUserByUsername() in MOATUserDetailsService.");

    try {
      final AdminDTO admin = adminService.selectByUsername(username);

      String password = admin.getPassword();

      return User.withUsername(username)
          .password(password)
          .roles("ADMIN", "USER")
          .build();
    } catch (NoResultException e) {
      logger.error("Admin username not found!");
    } catch (Exception e) {
      logger.error("An unknown error occurred trying to login!");
    }

    throw new UsernameNotFoundException(username);
  }
}