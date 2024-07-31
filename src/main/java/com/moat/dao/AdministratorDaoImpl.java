package com.moat.dao;

import com.moat.entity.Administrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.lang.String.format;

// TODO: Fix
@Repository("administratorDao")
public class AdministratorDaoImpl implements AdministratorDao {
  private final static Logger logger =
      LoggerFactory.getLogger(AdministratorDaoImpl.class);

  @PersistenceContext
  private EntityManager em;

  public AdministratorDaoImpl() {
    logger.debug("Constructing AdministratorDaoImpl.");
  }

  public Administrator selectById(int id) throws NoResultException {
    logger.debug("In selectById() in AdministratorDaoImpl");
    logger.info(format("Finding Administrator where id: %d.", id));

    Administrator administrator =
        em.createQuery("SELECT a FROM Administrator a WHERE a.id = :id",
            Administrator.class).setParameter("id", id).getSingleResult();

    return administrator;
  }

  public Administrator selectByUsername(String username)
      throws NoResultException {
    logger.info("In selectByUsername() in AdministratorDaoImpl.");
    logger.info(format("Finding Administrator where username: %s.", username));

    Administrator administrator = em.createQuery(
            "SELECT a FROM Administrator a WHERE a.username = :username",
            Administrator.class)
        .setParameter("username", username)
        .getSingleResult();

    return administrator;
  }

  public List<Administrator> selectAll() {
    logger.debug("In selectAll() in AdministratorDaoImpl.");

    List<Administrator> administrators =
        em.createQuery("SELECT a FROM Administrator a", Administrator.class)
            .getResultList();

    return administrators;
  }

  public void save(Administrator administrator) {
    logger.debug("In saveOrUpdate() in AdministratorDaoImpl.");

    if (administrator.getId() == 0)
      em.persist(administrator);
    else
      em.merge(administrator);
  }

  public void delete(Administrator administrator) {
    logger.debug("In delete() in AdministratorDaoImpl.");

    em.remove(administrator);
  }
}
