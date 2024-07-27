package com.moat.dao;

import com.moat.entity.Administrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("administratorDao")
@Transactional
public class AdministratorDaoImpl implements AdministratorDao {
  private Logger logger = LoggerFactory.getLogger(AdministratorDaoImpl.class);

  @PersistenceContext
  EntityManager em;

  public AdministratorDaoImpl() {
    logger.info("Constructing AdministratorDaoImpl.");
  }

  @Transactional(readOnly = true)
  public Administrator selectById(int id) throws NoResultException {
    logger.info("In selectById() in AdministratorDaoImpl");
    logger.info("Finding Administrator with ID=" + id);

    Administrator administrator =
        em.createQuery("SELECT a FROM Administrator a WHERE a.id = :id",
            Administrator.class).setParameter("id", id).getSingleResult();

    return administrator;
  }

  public Administrator selectByUsername(String username)
      throws NoResultException {
    logger.info("In selectByUsername() in AdministratorDaoImpl.");
    logger.info("Finding Administrator with username=" + username);

    Administrator administrator = em.createQuery(
            "SELECT a FROM Administrator a WHERE a.username = :username",
            Administrator.class).setParameter("username", username)
        .getSingleResult();

    return administrator;
  }

  @Transactional(readOnly = true)
  public List<Administrator> selectAll() {
    logger.info("In findAll() in AdministratorDaoImpl.");
    logger.info("Finding all Administrators.");

    List<Administrator> administrators =
        em.createQuery("SELECT a FROM Administrator a", Administrator.class)
            .getResultList();

    return administrators;
  }

  public void save(Administrator administrator) {
    logger.info("In saveOrUpdate() in AdministratorDaoImpl.");
    logger.info("Saving administrator.");

    if (administrator.getId() == 0)
      em.persist(administrator);
    else
      em.merge(administrator);
  }

  public void delete(Administrator administrator) {
    logger.info("In delete() in AdministratorDaoImpl.");

    em.remove(administrator);
  }
}
