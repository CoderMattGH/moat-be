package com.moat.service;

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
public class AdministratorServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorServiceTest.class);

    @PersistenceContext
    EntityManager em;

    @Autowired
    AdministratorService administratorService;

    public AdministratorServiceTest() {
        logger.info("Constructing AdministratorServiceTest.");
    }

    @Test
    public void testFindById_Valid() {
        Administrator administrator = administratorService.findById(1);

        assertEquals("admin", administrator.getUsername());
        assertEquals(1, administrator.getId());
    }

    @Test
    public void testFindById_InValid() {
        Administrator administrator = administratorService.findById(5);

        assertNull(administrator);
    }

    @Test
    public void testFindByUsername_Valid() {
        Administrator administrator = administratorService.findByUsername("admin");

        assertEquals("admin", administrator.getUsername());
        assertEquals(1, administrator.getId());
    }

    @Test
    public void testFindByUsername_InValid() {
        Administrator administrator = administratorService.findByUsername("invalid");

        assertNull(administrator);
    }

    @Test
    public void testFindAll() {
        List<Administrator> admins = administratorService.findAll();

        assertEquals(1, admins.size());
        assertEquals("admin", admins.stream().iterator().next().getUsername());
    }

    @Test
    public void testSaveOrUpdate_ReplaceMerge() {
        Administrator administrator = new Administrator("newname", "password");
        administrator.setId(1);

        administratorService.saveOrUpdate(administrator);
        em.flush();

        Administrator result = administratorService.findById(1);

        assertEquals("newname", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    public void testSaveOrUpdate_AddSave() {
        Administrator administrator = new Administrator("newadmin", "password");

        administratorService.saveOrUpdate(administrator);
        em.flush();

        Administrator result = administratorService.findById(2);

        assertEquals("newadmin", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    public void testSaveOrUpdate_InvalidSave() {
        // NULL username and password.
        Administrator administrator = new Administrator();

        boolean threwException = false;

        try {
            administratorService.saveOrUpdate(administrator);
        } catch (ConstraintViolationException ex) {
            threwException = true;
        }

        assertTrue(threwException);
    }

    @Test
    public void testDelete() {
        Administrator administrator = administratorService.findById(1);

        administratorService.delete(administrator);
        em.flush();

        Administrator result = administratorService.findById(1);

        assertNull(result);
    }
}
