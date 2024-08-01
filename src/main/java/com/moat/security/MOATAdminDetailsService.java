package com.moat.security;

import com.moat.constant.ValidationMsg;
import com.moat.dto.AdminDTO;
import com.moat.entity.MOATAdmin;
import com.moat.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service("moatAdminDetailsService")
public class MOATAdminDetailsService implements UserDetailsService {
  private final static Logger logger =
      LoggerFactory.getLogger(MOATAdminDetailsService.class);

  private final AdminService adminService;

  public MOATAdminDetailsService(AdminService adminService) {
    logger.debug("Constructing MOATAdminDetailsService.");

    this.adminService = adminService;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    logger.debug("In loadUserByUsername() in MOATAdminDetailsService.");

    final AdminDTO admin;

    try {
      admin = adminService.selectByUsername(username);
    } catch (NoResultException e) {
      throw new UsernameNotFoundException(ValidationMsg.ADMIN_DOES_NOT_EXIST);
    }

    MOATAdmin loggedAdmin = new MOATAdmin();
    loggedAdmin.setId(admin.getId());
    loggedAdmin.setUsername(admin.getUsername());
    loggedAdmin.setPassword(admin.getPassword());
    loggedAdmin.setEmail(admin.getEmail());
    loggedAdmin.setVerified(admin.isVerified());

    return new SecMOATAdminDetails(loggedAdmin);
  }
}
