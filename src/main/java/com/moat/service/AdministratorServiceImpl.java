package com.moat.service;

import com.moat.entity.Administrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class AdministratorServiceImpl implements AdministratorService {
    Logger logger = LoggerFactory.getLogger(AdministratorServiceImpl.class);

    @PersistenceContext
    EntityManager em;

    public AdministratorServiceImpl() {
        logger.info("Constructing AdministratorServiceImpl.");
    }

    @Transactional(readOnly=true)
    public Administrator findById(int id) {
        logger.info("Finding Administrator with ID=" + id);

        Administrator administrator = null;

        List<Administrator> administratorList = em.createQuery(
                    "SELECT a FROM Administrator a WHERE a.id = :id", Administrator.class)
                    .setParameter("id", id)
                    .getResultList();

        if (!administratorList.isEmpty())
            administrator = administratorList.get(0);

        return administrator;
    }

    public Administrator findByUsername(String username) {
        logger.info("Finding Administrator with username=" + username);

        Administrator administrator = null;

        List<Administrator> administratorList = em.createQuery(
         "SELECT a FROM Administrator a WHERE a.username = :username", Administrator.class)
                .setParameter("username", username)
                .getResultList();

        if (!administratorList.isEmpty())
            administrator = administratorList.get(0);

        if (administrator == null)
            logger.info("Administrator with username=" + username + " not found!");
        else
            logger.info("Administrator with username=" + username +  " found!");

        return administrator;
    }

    @Transactional(readOnly=true)
    public List<Administrator> findAll() {
        logger.info("Finding all Administrators.");

        List<Administrator> administrators = em.createQuery(
            "SELECT a FROM Administrator a", Administrator.class).getResultList();

        return administrators;
    }

    public void saveOrUpdate(Administrator administrator) {
        logger.info("Saving administrator.");

        if (administrator.getId() == 0)
            em.persist(administrator);
        else
            em.merge(administrator);
    }

    public void delete(Administrator administrator) {
        logger.info("Deleting administrator.");

        em.remove(administrator);
    }
}
