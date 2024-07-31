package com.moat.service;

import com.moat.constant.ValidationMsg;
import com.moat.dao.AdminDao;
import com.moat.dto.AdminDTO;
import com.moat.entity.MOATAdmin;
import com.moat.exception.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {
  private static final Logger logger =
      LoggerFactory.getLogger(AdminServiceImpl.class);

  private final AdminDao adminDao;

  public AdminServiceImpl(AdminDao adminDao) {
    logger.debug("Constructing AdminServiceImpl.");

    this.adminDao = adminDao;
  }

  @Transactional(readOnly = true)
  public List<AdminDTO> selectAll() throws NoResultException {
    logger.debug("In selectAll() in AdminServiceImpl.");

    List<MOATAdmin> admins = adminDao.selectAll();

    if (admins.isEmpty()) {
      throw new NoResultException();
    }

    return marshallIntoDTO(admins);
  }

  @Transactional(readOnly = true)
  public AdminDTO selectById(int id) throws NoResultException {
    logger.debug("In selectById() in AdminServiceImpl.");

    MOATAdmin admin;
    try {
      admin = adminDao.selectById(id);
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.ADMIN_DOES_NOT_EXIST);
    }

    return marshallIntoDTO(admin);
  }

  public AdminDTO selectByUsername(String username) throws NoResultException {
    logger.debug("In selectByUsername() in AdminServiceImpl.");

    MOATAdmin admin;
    try {
      admin = adminDao.selectByUsername(username);
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.ADMIN_DOES_NOT_EXIST);
    }

    return marshallIntoDTO(admin);
  }

  public AdminDTO create(MOATAdmin admin) throws AlreadyExistsException {
    logger.debug("In createAdministrator in AdminServiceImpl.");

    boolean adminExists = true;
    try {
      adminDao.selectByUsername(admin.getUsername());
    } catch (NoResultException e) {
      adminExists = false;
    }

    if (adminExists) {
      throw new AlreadyExistsException(ValidationMsg.ADMIN_ALREADY_EXISTS);
    }

    // TODO: Check email

    adminDao.saveOrUpdate(admin);

    return marshallIntoDTO(admin);
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public AdminDTO marshallIntoDTO(MOATAdmin admin) {
    return new AdminDTO(admin.getId(), admin.getUsername(), admin.getEmail());
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<AdminDTO> marshallIntoDTO(List<MOATAdmin> admins) {
    List<AdminDTO> dtos = new ArrayList<>();

    for (MOATAdmin admin : admins) {
      dtos.add(marshallIntoDTO(admin));
    }

    return dtos;
  }
}
