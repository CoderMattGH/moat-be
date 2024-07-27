package com.moat.service;

import com.moat.dao.UserDao;
import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;
import com.moat.exception.MOATValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
  private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private UserDao userDao;

  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  public List<MOATUser> selectAllUsers() {
    logger.info("In selectAllUsers() in userServiceImpl.");

    return userDao.selectAllUsers();
  }

  public MOATUser selectByUsername(String username) throws NoResultException {
    logger.info("In selectByUsername() in userServiceImpl.");

    return userDao.selectUserByUsername(username);
  }

  @Transactional
  public void createUser(MOATUser user)
      throws AlreadyExistsException, MOATValidationException {
    logger.info("In createUser() in userServiceImpl.");

    if (user.getId() != null) {
      throw new MOATValidationException("User ID must be null!");
    }

    // Check username doesn't already exist
    boolean userExists = true;
    try {
      userDao.selectUserByUsername(user.getUsername());
    } catch (NoResultException e) {
      userExists = false;
    }

    if (userExists) {
      throw new AlreadyExistsException("User already exists!");
    }

    userDao.saveUser(user);
  }
}
