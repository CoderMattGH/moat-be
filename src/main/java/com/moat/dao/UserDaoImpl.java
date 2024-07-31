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

  public void saveOrUpdate(MOATUser user) {
    logger.debug("In saveOrUpdate() in UserDaoImpl.");

    if (user.getId() == null) {
      em.persist(user);
    } else {
      em.merge(user);
    }
  }

  public List<MOATUser> selectAll() throws NoResultException {
    logger.debug("In selectAll() in UserDaoImpl.");

    return em.createQuery("SELECT u FROM MOATUser u", MOATUser.class)
        .getResultList();
  }

  public MOATUser selectByEmail(String email) throws NoResultException {
    logger.debug("In selectByEmail in UserDaoImpl.");

    return em.createQuery("SELECT u FROM MOATUser u WHERE u.email = :email",
        MOATUser.class).setParameter("email", email).getSingleResult();
  }

  public MOATUser selectById(Long id) throws NoResultException {
    logger.debug("In selectById in UserDaoImpl.");

    return em.createQuery("SELECT u FROM MOATUser u WHERE u.id = :id",
        MOATUser.class).setParameter("id", id).getSingleResult();
  }

  public MOATUser selectByUsername(String username) throws NoResultException {
    logger.debug("In selectByUsername() in UserDaoImpl.");

    return em.createQuery(
            "SELECT u FROM MOATUser u WHERE u.username = :username", MOATUser.class)
        .setParameter("username", username)
        .getSingleResult();
  }
}
