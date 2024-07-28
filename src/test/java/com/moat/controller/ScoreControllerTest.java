package com.moat.controller;

import org.apache.tomcat.util.file.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScoreControllerTest {
  private final static Logger logger =
      LoggerFactory.getLogger(ScoreControllerTest.class);

  @Autowired
  private MockMvc mvc;

  @Test
  public void get_scores_returns_valid_score_objects() throws Exception {
    logger.info("Test scores are returned OK.");

    mvc.perform(MockMvcRequestBuilders.get("/score/")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.scores[*].id").isArray())
        .andExpect(jsonPath("$.scores[*].id").isNotEmpty())
        .andExpect(jsonPath("$.scores[*].id",
            everyItem(allOf(instanceOf(Number.class), greaterThan(0)))))
        .andExpect(jsonPath("$.scores[*].score").isNotEmpty())
        .andExpect(
            jsonPath("$.scores[*].score", everyItem(instanceOf(Number.class))))
        .andExpect(jsonPath("$.scores[*].username").isNotEmpty())
        .andExpect(jsonPath("$.scores[*].username",
            everyItem(allOf(instanceOf(String.class), not(emptyString())))))
        .andExpect(jsonPath("$.scores[*].userId").isNotEmpty())
        .andExpect(jsonPath("$.scores[*].userId",
            everyItem(allOf(instanceOf(Number.class), greaterThan(0)))));
  }
}
