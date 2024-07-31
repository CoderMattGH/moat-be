package com.moat.dao;

import com.moat.entity.MOATAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.lang.String.format;

@Repository("adminDao")
public class AdminDaoImpl implements AdminDao {
  private final static Logger logger =
      LoggerFactory.getLogger(AdminDaoImpl.class);

  @PersistenceContext
  private EntityManager em;

  public AdminDaoImpl() {
    logger.debug("Constructing AdministratorDaoImpl.");
  }

  public MOATAdmin selectById(Long id) throws NoResultException {
    logger.debug("In selectById() in AdministratorDaoImpl");
    logger.info(format("Finding Administrator where id: %d.", id));

    return em.createQuery("SELECT a FROM MOATAdmin a WHERE a.id = :id",
        MOATAdmin.class).setParameter("id", id).getSingleResult();
  }

  public MOATAdmin selectByUsername(String username) throws NoResultException {
    logger.info("In selectByUsername() in AdministratorDaoImpl.");
    logger.info(format("Finding Administrator where username: %s.", username));

    return em.createQuery(
        "SELECT a FROM MOATAdmin a WHERE a.username = :username",
        MOATAdmin.class).setParameter("username", username).getSingleResult();
  }

  public List<MOATAdmin> selectAll() {
    logger.debug("In selectAll() in AdministratorDaoImpl.");

    return em.createQuery("SELECT a FROM MOATAdmin a", MOATAdmin.class)
        .getResultList();
  }

  public void saveOrUpdate(MOATAdmin admin) {
    logger.debug("In saveOrUpdate() in AdministratorDaoImpl.");

    if (admin.getId() == null)
      em.persist(admin);
    else
      em.merge(admin);
  }

  public int deleteById(Long id) {
    logger.debug("In delete() in AdministratorDaoImpl.");

    return em.createQuery("DELETE FROM MOATAdmin a WHERE a.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }
}
