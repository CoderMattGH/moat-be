package com.moat.service;

import com.moat.dao.AdministratorDao;
import com.moat.entity.Administrator;
import com.moat.exception.MOATValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Service("administratorService")
public class AdministratorServiceImpl implements AdministratorService {
  private final Logger logger =
      LoggerFactory.getLogger(AdministratorServiceImpl.class);

  AdministratorDao administratorDao;

  public AdministratorServiceImpl(AdministratorDao administratorDao) {
    logger.info("Constructing AdministratorServiceImpl.");

    this.administratorDao = administratorDao;
  }

  public List<Administrator> selectAll() throws NoResultException {
    logger.info("In selectAll() in AdministratorServiceImpl.");

    List<Administrator> administrators = administratorDao.selectAll();

    if (administrators.isEmpty()) {
      throw new NoResultException();
    }

    return administrators;
  }

  public Administrator selectById(int id) throws NoResultException {
    logger.info("In selectById() in AdministratorServiceImpl.");

    return administratorDao.selectById(id);
  }

  public Administrator selectByUsername(String username)
      throws NoResultException {
    logger.info("In selectByUsername() in AdministratorServiceImpl.");

    return administratorDao.selectByUsername(username);
  }

  @Transactional
  public void createAdministrator(Administrator administrator)
      throws MOATValidationException {
    logger.info("In createAdministrator in AdministratorServiceImpl.");

    if (administrator.getId() != null) {
      throw new MOATValidationException("Administrator ID must be null!");
    }

    // TODO: Check doesn't exist

    administratorDao.save(administrator);
  }
}
