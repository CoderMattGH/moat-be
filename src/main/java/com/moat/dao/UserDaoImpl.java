package com.moat.dao;

import com.moat.entity.MOATUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
  private final static Logger logger =
      LoggerFactory.getLogger(UserDaoImpl.class);

  @PersistenceContext
  private EntityManager em;

  public List<MOATUser> selectAllUsers() {
    logger.info("In selectAllUsers() in UserDaoImpl.");

    return em.createQuery("SELECT u FROM MOATUser u", MOATUser.class)
        .getResultList();
  }

  public MOATUser selectUserByUsername(String username)
      throws NoResultException {
    logger.info("In selectUserByUsername() in UserDaoImpl.");

    return em.createQuery(
            "SELECT u FROM MOATUser u where u.username = :username", MOATUser.class)
        .setParameter("username", username)
        .getSingleResult();
  }

  public void saveUser(MOATUser user) {
    logger.info("In createUser() in UserDaoImpl.");

    if (user.getId() == null) {
      em.persist(user);
    } else {
      em.merge(user);
    }
  }
}
