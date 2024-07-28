package com.moat.service;

import com.moat.dao.UserDao;
import com.moat.dto.UserDTO;
import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;
import com.moat.exception.MOATValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {
  private final static Logger logger =
      LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserDao userDao;

  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Transactional(readOnly = true)
  public List<MOATUser> selectAllUsers() {
    logger.info("In selectAllUsers() in userServiceImpl.");

    return userDao.selectAllUsers();
  }

  @Transactional(readOnly = true)
  public MOATUser selectByUsername(String username) throws NoResultException {
    logger.info("In selectByUsername() in userServiceImpl.");

    return userDao.selectUserByUsername(username);
  }

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

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public UserDTO marshallIntoDTO(MOATUser user) {
    return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<UserDTO> marshallIntoDTO(List<MOATUser> users) {
    List<UserDTO> dtos = new ArrayList<>();

    for (MOATUser user : users) {
      dtos.add(marshallIntoDTO(user));
    }

    return dtos;
  }
}
