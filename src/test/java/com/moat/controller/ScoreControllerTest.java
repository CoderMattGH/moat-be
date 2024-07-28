package com.moat.controller;

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
        .andExpect((status().isOk()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.scores[*].id").isArray())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.scores[*].id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.scores[*].id",
            everyItem(any(Number.class))))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.scores[*].score").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.scores[*].score",
            everyItem(any(Number.class))))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.scores[*].username").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.scores[*].username",
            everyItem(any(String.class))))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.scores[*].userId").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.scores[*].userId",
            everyItem(any(Number.class))));
  }
}
