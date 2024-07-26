package com.moat.service;

import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service("userService")
@Transactional
@Repository
public class UserServiceImpl implements UserService {
  private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @PersistenceContext
  private EntityManager em;

  @Transactional(readOnly = true)
  public List<MOATUser> selectAllUsers() {
    logger.info("In selectAllUsers() in UserServiceImpl.");

    return em.createQuery("SELECT u FROM MOATUser u", MOATUser.class).getResultList();
  }

  @Transactional(readOnly = true)
  public MOATUser selectUserByUsername(String username) throws NoResultException {
    logger.info("In selectUserByUsername() in UserServiceImpl.");

    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty!");
    }

    return em.createQuery("SELECT u FROM MOATUser u where u.username = :username", MOATUser.class)
        .setParameter("username", username).getSingleResult();
  }

  // TODO: Validation
  public MOATUser createUser(MOATUser user) throws AlreadyExistsException {
    logger.info("In createUser() in UserServiceImpl.");

    // Check username doesn't already exist
    boolean userExists = true;
    try {
      selectUserByUsername(user.getUsername());
    } catch (NoResultException e) {
      userExists = false;
    }

    if (userExists) {
      throw new AlreadyExistsException("User already exists!");
    }

    em.persist(user);

    System.out.println("SAVED userID:" + user.getId());

    return user;
  }
}
