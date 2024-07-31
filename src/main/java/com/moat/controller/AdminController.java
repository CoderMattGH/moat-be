package com.moat.controller;

import com.moat.constant.ValidationMsg;
import com.moat.dto.AdminDTO;
import com.moat.exception.AlreadyExistsException;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import com.moat.service.AdminService;
import com.moat.validator.group.SaveAdminGroup;
import com.moat.validator.misc.UsernameValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@Validated
@RequestMapping(value = "/admin")
public class AdminController {
  private final static Logger logger =
      LoggerFactory.getLogger(AdminController.class);

  private final AdminService adminService;
  private final DynamicResponseWrapperFactory resFact;

  public AdminController(AdminService adminService,
      DynamicResponseWrapperFactory resFact) {
    logger.debug("Constructing AdminController.");

    this.adminService = adminService;
    this.resFact = resFact;
  }

  @GetMapping("/")
  public ResponseEntity<?> getAdmins() {
    logger.debug("In getAdmins() in AdminController.");

    List<AdminDTO> admins;

    try {
      admins = adminService.selectAll();
    } catch (NoResultException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_GETTING_ADMIN,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Remove sensitive fields
    admins.forEach(admin -> admin.setPassword(null));

    return resFact.build("admins", admins, HttpStatus.OK);
  }

  @GetMapping("/{username}/")
  public ResponseEntity<?> getByAdminUsername(
      @PathVariable @UsernameValid String username) {
    logger.debug("In getByAdminId() in AdminController.");

    AdminDTO admin;

    try {
      admin = adminService.selectByUsername(username);
    } catch (NoResultException e) {
      return resFact.build("message", ValidationMsg.ADMIN_DOES_NOT_EXIST,
          HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_GETTING_ADMIN,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Remove sensitive fields
    admin.setPassword(null);

    return resFact.build("admin", admin, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<?> postAdmin(
      @RequestBody @Validated(SaveAdminGroup.class) AdminDTO admin) {
    logger.debug("In postAdmin() in AdminController.");

    AdminDTO newAdmin;

    try {
      newAdmin = adminService.create(admin);
    } catch (AlreadyExistsException e) {
      return resFact.build("message", ValidationMsg.ADMIN_ALREADY_EXISTS,
          HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_POSTING_ADMIN,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Remove sensitive fields
    newAdmin.setPassword(null);

    return resFact.build("admin", newAdmin, HttpStatus.OK);
  }
}