package com.moat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moat.constant.ValidationMsg;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

  @Nested
  @DisplayName("DELETE /score/")
  class deleteScoreTests {
    @Test
    @DisplayName("Successfully deletes all scores.")
    public void delete_score_valid_request_all_scores_deleted()
        throws Exception {
      mvc.perform(MockMvcRequestBuilders.delete("/score/"))
          .andExpect(status().isOk());

      // Verify scores have been deleted
      mvc.perform(MockMvcRequestBuilders.get("/score/"))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.SCORES_NOT_FOUND));
    }
  }

  @Nested
  @DisplayName("DELETE /score/{userId}")
  class deleteScoreUserId {
    @Test
    @DisplayName(
        "When given a valid userId successfully deletes all their scores.")
    public void by_user_id_valid_request_user_scores_deleted()
        throws Exception {
      int userId = 1;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/"))
          .andExpect(status().isOk());

      // Verify scores have been deleted
      mvc.perform(MockMvcRequestBuilders.get("/score/" + userId + "/"))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath(("$.message")).value(ValidationMsg.SCORES_NOT_FOUND));
    }

    @Test
    @DisplayName("When given a non extant userId returns 404 not found.")
    public void non_extant_user_return_not_found() throws Exception {
      int userId = 999;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/"))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("When userId is decimal returns bad request.")
    public void when_userid_decimal_number_return_bad_request()
        throws Exception {
      double userId = 1.1;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is not a number returns bad request.")
    public void when_userid_not_number_return_bad_request() throws Exception {
      String userId = "banana";

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When user has no scores returns 404 not found.")
    public void when_user_has_no_scores_return_bad_request() throws Exception {
      int userId = 3;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/"))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.SCORES_NOT_FOUND));
    }
  }

  @Nested
  @DisplayName("GET /score/")
  class getScoreTests {
    @Test
    @DisplayName("Scores are returned OK.")
    public void returns_valid_score_objects() throws Exception {
      mvc.perform(MockMvcRequestBuilders.get("/score/")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.scores[*].id").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].id",
              everyItem(allOf(instanceOf(Number.class), greaterThan(0)))))
          .andExpect(jsonPath("$.scores[*].score").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].score",
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].username").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].username", everyItem(
              allOf(instanceOf(String.class), not(emptyOrNullString())))))
          .andExpect(jsonPath("$.scores[*].userId").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].userId",
              everyItem(allOf(instanceOf(Number.class), greaterThan(0)))));
    }
  }

  @Nested
  @DisplayName("GET /score/{userId}/")
  class getScoreByUserIdTests {
    @Test
    @DisplayName("Valid userId returns all user's scores.")
    public void valid_userid_returns_users_scores() throws Exception {
      int userId = 1;

      mvc.perform(MockMvcRequestBuilders.get("/score/" + userId + "/")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.scores[*]").isNotEmpty())
          .andExpect(
              jsonPath("$.scores[*].id", everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].id", everyItem(greaterThan(0))))
          .andExpect(jsonPath("$.scores[*].score").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].score",
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].username").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].username",
              everyItem(instanceOf(String.class))))
          .andExpect(jsonPath("$.scores[*].username",
              everyItem(not(emptyOrNullString()))))
          .andExpect(jsonPath("$.scores[*].userId").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].userId",
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].userId", everyItem(is(userId))));
    }

    @Test
    @DisplayName("When userId is not a number return bad request.")
    public void userid_is_not_number_return_bad_request() throws Exception {
      String userId = "banana";

      mvc.perform(MockMvcRequestBuilders.get("/score/" + userId + "/")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is a decimal return bad request.")
    public void userid_is_decimal_number_return_bad_request() throws Exception {
      double userId = 1.1;

      mvc.perform(MockMvcRequestBuilders.get("/score/" + userId + "/")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When user does not exist return 404 not found.")
    public void user_doesnt_exist_return_not_found() throws Exception {
      int userId = 9999;

      mvc.perform(MockMvcRequestBuilders.get("/score/" + userId + "/")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("When user does not have any scores return 404 not found.")
    public void user_has_no_scores_return_not_found() throws Exception {
      int userId = 3;

      mvc.perform(MockMvcRequestBuilders.get("/score/" + userId + "/")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.SCORES_NOT_FOUND));
    }
  }

  @Nested
  @DisplayName("POST /score/")
  class postScoreTests {
    @Test
    @DisplayName("When score is below 0 return bad request.")
    public void below_zero_score_returns_bad_request() throws Exception {
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
          .andExpect(
              jsonPath("$.message").value("Score must be bigger than 0!"));
    }

    @Test
    @DisplayName("When score is decimal return bad request.")
    public void decimal_score_returns_bad_request() throws Exception {
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
    @DisplayName("When userId is a decimal return bad request.")
    public void decimal_userId_returns_bad_request() throws Exception {
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
    @DisplayName("When given a malformed JSON object then return bad request.")
    public void malformed_json_returns_bad_request() throws Exception {
      // Missing closing brace
      String json = "{\"userId\": 1, \"score\": 200";

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.JSON_PARSE_ERROR));
    }

    @Test
    @DisplayName("When score is not a number return bad request.")
    public void nan_score_returns_bad_request() throws Exception {
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
    @DisplayName("When userId is not a number return bad request.")
    public void nan_userId_returns_bad_request() throws Exception {
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
    public void negative_userId_returns_bad_request() throws Exception {
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
    @DisplayName("When userId does not exist return 404 not found.")
    public void non_extant_userId_returns_bad_request() throws Exception {
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
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("When score is not defined return bad request.")
    public void not_defined_score_returns_bad_request() throws Exception {
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
    @DisplayName("When score is null return bad request.")
    public void null_score_returns_bad_request() throws Exception {
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
    @DisplayName("When userId is null return bad request.")
    public void null_userId_returns_bad_request() throws Exception {
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
    @DisplayName("Valid scores are returned OK.")
    public void returns_valid_score_object() throws Exception {
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
    @DisplayName("When userId is undefined return bad request.")
    public void undefined_userId_returns_bad_request() throws Exception {
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
  }
}
