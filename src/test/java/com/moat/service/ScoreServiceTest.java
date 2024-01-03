package com.moat.service;

import com.moat.entity.Score;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for ScoreService.
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class ScoreServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ScoreServiceTest.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ScoreService scoreService;

    public ScoreServiceTest() {
        logger.info("Constructing ScoreServiceTest");
    }

    @Test
    public void testSave() {
        Score score = new Score(4000, "newscore");

        scoreService.save(score);

        assertNotNull(score.getId());
    }

    @Test
    public void testDelete_Valid() {
        Score score = new Score(3000, "paulp");
        score.setId(3L);

        scoreService.delete(score);

        List<Score> result = scoreService.findScoresByNickname("paulp");

        assertEquals(0, result.size());
    }

    @Test
    public void testFindAll() {
        List<Score> result = scoreService.findAll();

        assertEquals(5, result.size());
    }

    @Test
    public void testFindTopTenScoresSorted() {
        List<Score> result = scoreService.findTopTenScoresSorted();

        assertEquals(5, result.size());
        assertEquals(5, result.stream().findFirst().get().getId());
        assertEquals(1000, result.get(4).getScore());
    }

    @Test
    public void testFindScoresByNickname() {
        List<Score> result = scoreService.findScoresByNickname("peterp");

        assertEquals(1, result.size());
        assertEquals(4, result.stream().findFirst().get().getId());
    }
}
