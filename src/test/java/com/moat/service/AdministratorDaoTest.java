package com.moat.service;

import com.moat.dao.AdministratorDao;
import com.moat.entity.Administrator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test for the AdministratorService class.
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class AdministratorDaoTest {
  private static final Logger logger =
      LoggerFactory.getLogger(AdministratorDaoTest.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private AdministratorDao administratorDao;

  public AdministratorDaoTest() {
    logger.info("Constructing AdministratorServiceTest.");
  }

  @Test
  public void testFindById_Valid() {
    Administrator administrator = administratorDao.findById(1);

    assertEquals("admin", administrator.getUsername());
    assertEquals(1, administrator.getId());
  }

  @Test
  public void testFindById_InValid() {
    Administrator administrator = administratorDao.findById(5);

    assertNull(administrator);
  }

  @Test
  public void testFindByUsername_Valid() {
    Administrator administrator = administratorDao.findByUsername("admin");

    assertEquals("admin", administrator.getUsername());
    assertEquals(1, administrator.getId());
  }

  @Test
  public void testFindByUsername_InValid() {
    Administrator administrator = administratorDao.findByUsername("invalid");

    assertNull(administrator);
  }

  @Test
  public void testFindAll() {
    List<Administrator> admins = administratorDao.findAll();

    assertEquals(1, admins.size());
    assertEquals("admin", admins.stream().iterator().next().getUsername());
  }

  @Test
  public void testSaveOrUpdate_ReplaceMerge() {
    Administrator administrator = new Administrator("newname", "password");
    administrator.setId(1);

    administratorDao.saveOrUpdate(administrator);
    em.flush();

    Administrator result = administratorDao.findById(1);

    assertEquals("newname", result.getUsername());
    assertEquals("password", result.getPassword());
  }

  @Test
  public void testSaveOrUpdate_AddSave() {
    Administrator administrator = new Administrator("newadmin", "password");

    administratorDao.saveOrUpdate(administrator);
    em.flush();

    Administrator result = administratorDao.findById(2);

    assertEquals("newadmin", result.getUsername());
    assertEquals("password", result.getPassword());
  }

  @Test
  public void testSaveOrUpdate_InvalidSave() {
    // NULL username and password.
    Administrator administrator = new Administrator();

    boolean threwException = false;

    try {
      administratorDao.saveOrUpdate(administrator);
    } catch (ConstraintViolationException ex) {
      threwException = true;
    }

    assertTrue(threwException);
  }

  @Test
  public void testDelete() {
    Administrator administrator = administratorDao.findById(1);

    administratorDao.delete(administrator);
    em.flush();

    Administrator result = administratorDao.findById(1);

    assertNull(result);
  }
}
