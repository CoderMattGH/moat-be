package com.moat.service;

import com.moat.entity.MOATUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
  private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @PersistenceContext
  private EntityManager em;

  @Transactional(readOnly = true)
  public List<MOATUser> selectAllUsers() {
    logger.info("In selectAllUsers() in UserServiceImpl.");

    return em.createQuery("SELECT u FROM MOATUser u", MOATUser.class).getResultList();
  }
}
