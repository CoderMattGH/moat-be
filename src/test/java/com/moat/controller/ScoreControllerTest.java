package com.moat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moat.constant.ValidationMsg;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Scores are returned OK.")
  public void get_scores_returns_valid_score_objects() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/score/")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
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

  @Test
  @DisplayName("Valid scores are returned OK.")
  public void post_score_returns_valid_score_object() throws Exception {
    int score = 100;
    Long userId = 1L;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("userId", userId);
    scoreMap.put("score", score);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.score.id").value(greaterThan(0)))
        .andExpect(jsonPath("$.score.score").value(greaterThanOrEqualTo(0)))
        .andExpect(jsonPath("$.score.userId").value(userId))
        .andExpect(jsonPath("$.score.username").value(not(emptyString())));
  }

  @Test
  @DisplayName("When score is not defined return bad request.")
  public void post_score_not_defined_score_returns_bad_request()
      throws Exception {
    int userId = 1;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("userId", userId);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("score cannot be null!"));
  }

  @Test
  @DisplayName("When score is not a number return bad request.")
  public void post_score_nan_score_returns_bad_request() throws Exception {
    Long userId = 1L;
    String score = "Banana";

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("userId", userId);
    scoreMap.put("score", score);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect((status().isBadRequest()))
        .andExpect(
            jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
  }

  @Test
  @DisplayName("When score is below 0 return bad request.")
  public void post_score_below_zero_score_returns_bad_request()
      throws Exception {
    Long userId = 1L;
    int score = -1;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("userId", userId);
    scoreMap.put("score", score);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect((status().isBadRequest()))
        .andExpect(jsonPath("$.message").value("Score must be bigger than 0!"));
  }

  @Test
  @DisplayName("When score is null return bad request.")
  public void post_score_null_score_returns_bad_request() throws Exception {
    Long userId = 1L;
    Integer score = null;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("userId", userId);
    scoreMap.put("score", score);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect((status().isBadRequest()))
        .andExpect(jsonPath("$.message").value("score cannot be null!"));
  }

  @Test
  @DisplayName("When score is decimal return bad request.")
  public void post_score_decimal_score_returns_bad_request() throws Exception {
    int userId = 2;
    double score = 100.3;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("userId", userId);
    scoreMap.put("score", score);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect((status().isBadRequest()))
        .andExpect(
            jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
  }

  @Test
  @DisplayName("When userId is null return bad request.")
  public void post_score_null_userId_returns_bad_request() throws Exception {
    Long userId = null;
    int score = 100;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("userId", userId);
    scoreMap.put("score", score);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("userId cannot be null!"));
  }

  @Test
  @DisplayName("When userId is undefined return bad request.")
  public void post_score_undefined_userId_returns_bad_request()
      throws Exception {
    int score = 100;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("score", score);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("userId cannot be null!"));
  }

  @Test
  @DisplayName("When userId is not a number return bad request.")
  public void post_score_nan_userId_returns_bad_request() throws Exception {
    int score = 100;
    String userId = "banana";

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("score", score);
    scoreMap.put("userId", userId);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
  }

  @Test
  @DisplayName("When userId is negative return bad request.")
  public void post_score_negative_userId_returns_bad_request()
      throws Exception {
    int score = 100;
    int userId = -100;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("score", score);
    scoreMap.put("userId", userId);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message").value("userId cannot be less than 1!"));
  }

  @Test
  @DisplayName("When userId is a decimal return bad request.")
  public void post_score_decimal_userId_returns_bad_request() throws Exception {
    int score = 100;
    double userId = 1.2;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("score", score);
    scoreMap.put("userId", userId);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
  }

  @Test
  @DisplayName("When userId does not exist return 404 not found.")
  public void post_score_non_extant_userId_returns_bad_request()
      throws Exception {
    int score = 100;
    int userId = 9999;

    Map<String, Object> scoreMap = new HashMap<>();
    scoreMap.put("score", score);
    scoreMap.put("userId", userId);

    String json = objectMapper.writeValueAsString(scoreMap);

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(ValidationMsg.USER_DOES_EXIST));
  }

  @Test
  @DisplayName("When given a malformed JSON object then return bad request.")
  public void post_score_malformed_json_returns_bad_request() throws Exception {
    // Missing closing brace
    String json = "{\"userId\": 1, \"score\": 200";

    mvc.perform(MockMvcRequestBuilders.post("/score/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(ValidationMsg.JSON_PARSE_ERROR));
  }

  @Test
  @DisplayName("Successfully deletes all scores.")
  public void delete_score_valid_request_all_scores_deleted() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/score/"))
        .andExpect(status().isOk());

    // Verify scores have been deleted
    mvc.perform(MockMvcRequestBuilders.get("/score/"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(ValidationMsg.SCORES_NOT_FOUND));
  }
}
