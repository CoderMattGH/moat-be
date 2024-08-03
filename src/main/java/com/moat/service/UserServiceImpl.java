package com.moat.service;

import com.moat.constant.ValidationMsg;
import com.moat.dao.UserDao;
import com.moat.dto.UserDTO;
import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
    logger.debug("Constructing UserServiceImpl.");

    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDTO create(MOATUser user) throws AlreadyExistsException {
    logger.debug("In create(MOATUser) in UserServiceImpl.");

    // Check username doesn't already exist
    boolean userExists = true;
    try {
      userDao.selectByUsername(user.getUsername());
    } catch (NoResultException e) {
      userExists = false;
    }

    if (userExists) {
      throw new AlreadyExistsException(ValidationMsg.USER_ALREADY_EXISTS);
    }

    boolean emailExists = true;
    try {
      userDao.selectByEmail(user.getEmail());
    } catch (NoResultException e) {
      emailExists = false;
    }

    if (emailExists) {
      throw new AlreadyExistsException(ValidationMsg.EMAIL_ALREADY_EXISTS);
    }

    userDao.saveOrUpdate(user);

    return marshallIntoDTO(user);
  }

  public UserDTO create(UserDTO user) throws AlreadyExistsException {
    logger.debug("In create(UserDTO) in UserServiceImpl.");

    String encodedPassword = passwordEncoder.encode(user.getPassword());

    MOATUser moatUser = new MOATUser();
    moatUser.setPassword(encodedPassword);
    moatUser.setUsername(user.getUsername());
    moatUser.setEmail(user.getEmail());

    moatUser.setRole("USER");

    return create(moatUser);
  }

  /**
   * Updates a users email or username.
   */
  public UserDTO updateUserDetails(UserDTO user)
      throws AlreadyExistsException, NoResultException {
    logger.debug("In updateUserDetails() in UserServiceImpl.");

    MOATUser originalUser;

    try {
      originalUser = userDao.selectById(user.getId());
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.USER_DOES_NOT_EXIST);
    }

    // Check new username does not already exist
    String newUsername = user.getUsername();
    if (!originalUser.getUsername().equals(newUsername)) {
      boolean usernameExists = true;
      try {
        userDao.selectByUsername(newUsername);
      } catch (NoResultException e) {
        usernameExists = false;
      }

      if (usernameExists) {
        throw new AlreadyExistsException(ValidationMsg.USERNAME_ALREADY_EXISTS);
      }
    }

    // Check new email does not already exist.
    String newEmail = user.getEmail();
    if (!originalUser.getEmail().equals(newEmail)) {
      boolean emailExists = true;
      try {
        userDao.selectByEmail(newEmail);
      } catch (NoResultException e) {
        emailExists = false;
      }

      if (emailExists) {
        throw new AlreadyExistsException(ValidationMsg.EMAIL_ALREADY_EXISTS);
      }
    }

    originalUser.setEmail(newEmail);
    originalUser.setUsername(newUsername);

    userDao.saveOrUpdate(originalUser);

    return marshallIntoDTO(originalUser);
  }

  public UserDTO updateUserPassword(UserDTO user) throws NoResultException {
    logger.debug("In updateUserPassword() in UserServiceImpl.");

    MOATUser originalUser;

    try {
      originalUser = userDao.selectById(user.getId());
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.USER_DOES_NOT_EXIST);
    }

    String encodedPassword = passwordEncoder.encode(user.getPassword());
    originalUser.setPassword(encodedPassword);

    userDao.saveOrUpdate(originalUser);

    return marshallIntoDTO(originalUser);
  }

  @Transactional(readOnly = true)
  public List<UserDTO> selectAll() {
    logger.debug("In selectAll() in UserServiceImpl.");

    List<MOATUser> users = userDao.selectAll();

    if (users.isEmpty()) {
      throw new NoResultException(ValidationMsg.USERS_NOT_FOUND);
    }

    return marshallIntoDTO(users);
  }

  @Transactional(readOnly = true)
  public UserDTO selectById(Long id) throws NoResultException {
    logger.debug("In selectById() in UserServiceImpl");

    MOATUser user;
    try {
      user = userDao.selectById(id);
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.USER_DOES_NOT_EXIST);
    }

    return marshallIntoDTO(user);
  }

  @Transactional(readOnly = true)
  public UserDTO selectByUsername(String username) throws NoResultException {
    logger.debug("In selectByUsername() in UserServiceImpl.");

    MOATUser user;
    try {
      user = userDao.selectByUsername(username);
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.USER_DOES_NOT_EXIST);
    }

    return marshallIntoDTO(user);
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public UserDTO marshallIntoDTO(MOATUser user) {
    UserDTO dto = new UserDTO();

    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    dto.setPassword(user.getPassword());
    dto.setBanned(user.isBanned());
    dto.setVerified(user.isVerified());
    dto.setRole(user.getRole());

    return dto;
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
