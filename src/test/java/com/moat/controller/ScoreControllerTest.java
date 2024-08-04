package com.moat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

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

  public String getJwtToken(String username, String password) throws Exception {
    Map<String, Object> user = new HashMap<>();
    user.put("username", username);
    user.put("password", password);

    String json = objectMapper.writeValueAsString(user);

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String response = result.getResponse().getContentAsString();

    return JsonPath.parse(response).read("$.user.token");
  }

  @Nested
  @Transactional
  @DisplayName("DELETE /score/")
  class DeleteScoreTests {
    @Test
    @DisplayName("Admin deletes all scores successfully.")
    public void admin_request_all_scores_deleted() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      mvc.perform(MockMvcRequestBuilders.delete("/score/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk());

      // Verify scores have been deleted
      mvc.perform(MockMvcRequestBuilders.get("/score/"))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.SCORES_NOT_FOUND));
    }

    @Test
    @DisplayName("When user returns not forbidden.")
    public void user_request_forbidden() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      mvc.perform(MockMvcRequestBuilders.delete("/score/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isForbidden())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("When guest returns not authorized.")
    public void guest_request_not_authorized() throws Exception {
      mvc.perform(MockMvcRequestBuilders.delete("/score/"))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }
  }

  @Nested
  @Transactional
  @DisplayName("DELETE /score/{userId}/")
  class DeleteScoreUserId {
    @Test
    @DisplayName(
        "Admin given a valid userId successfully deletes all their scores.")
    public void admin_valid_request_user_scores_deleted() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      int userId = 1;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk());

      // Verify scores have been deleted
      mvc.perform(MockMvcRequestBuilders.get("/score/" + userId + "/"))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath(("$.message")).value(ValidationMsg.SCORES_NOT_FOUND));
    }

    @Test
    @DisplayName("User given a valid userId returns forbidden.")
    public void user_request_returns_forbidden() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int userId = 1;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isForbidden())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("Guest returns unauthorized.")
    public void guest_request_returns_unauthorized() throws Exception {
      int userId = 1;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/"))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("When given a non extant userId returns 404 not found.")
    public void non_extant_user_return_not_found() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      int userId = 999;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("When userId is decimal returns bad request.")
    public void when_userid_decimal_number_return_bad_request()
        throws Exception {
      String token = getJwtToken("ADMIN", "password");

      double userId = 1.1;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is not a number returns bad request.")
    public void when_userid_not_number_return_bad_request() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      String userId = "banana";

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When user has no scores returns 404 not found.")
    public void when_user_has_no_scores_return_bad_request() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      int userId = 3;

      mvc.perform(MockMvcRequestBuilders.delete("/score/" + userId + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.SCORES_NOT_FOUND));
    }
  }

  @Nested
  @Transactional
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
              everyItem(allOf(instanceOf(Number.class), greaterThan(0)))))
          .andExpect(jsonPath("$.scores[*].hits").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].hits").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].hits").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].notHits").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].notHits").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].notHits").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].misses").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].misses").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].misses").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].average").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(instanceOf(Double.class))))
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(greaterThanOrEqualTo(0.0))))
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(lessThanOrEqualTo(100.0))));
    }
  }

  @Nested
  @Transactional
  @DisplayName("GET /score/{userId}/")
  class GetScoreByUserIdTests {
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
          .andExpect(jsonPath("$.scores[*].userId", everyItem(is(userId))))
          .andExpect(jsonPath("$.scores[*].hits").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].hits").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].hits").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].notHits").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].notHits").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].notHits").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].misses").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].misses").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].misses").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].average").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(instanceOf(Double.class))))
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(greaterThanOrEqualTo(0.0))))
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(lessThanOrEqualTo(100.0))));
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
  @Transactional
  @DisplayName("POST /score/")
  class PostScoreTests {
    @Test
    @DisplayName("Admin valid score is returned OK.")
    public void admin_returns_valid_score_object() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      int score = 100;
      Long userId = 1L;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.score.id").value(greaterThan(0)))
          .andExpect(jsonPath("$.score.score").value(greaterThanOrEqualTo(0)))
          .andExpect(jsonPath("$.score.userId").value(userId))
          .andExpect(jsonPath("$.score.username").value(not(emptyString())))
          .andExpect(jsonPath("$.score.hits").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.hits").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.notHits").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.notHits").value(greaterThanOrEqualTo(0)))
          .andExpect(jsonPath("$.score.misses").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.misses").value(greaterThanOrEqualTo(0)))
          .andExpect(jsonPath("$.score.score").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.score").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.average").value(instanceOf(Double.class)))
          .andExpect(
              jsonPath("$.score.average").value(greaterThanOrEqualTo(0.0)))
          .andExpect(
              jsonPath("$.score.average").value(lessThanOrEqualTo(100.0)));
    }

    @Test
    @DisplayName("Matching user id with valid score is returned OK.")
    public void match_user_id_returns_valid_score_object() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.score.id").value(greaterThan(0)))
          .andExpect(jsonPath("$.score.score").value(greaterThanOrEqualTo(0)))
          .andExpect(jsonPath("$.score.userId").value(userId))
          .andExpect(jsonPath("$.score.username").value(not(emptyString())))
          .andExpect(jsonPath("$.score.hits").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.hits").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.notHits").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.notHits").value(greaterThanOrEqualTo(0)))
          .andExpect(jsonPath("$.score.misses").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.misses").value(greaterThanOrEqualTo(0)))
          .andExpect(jsonPath("$.score.score").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.score").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.average").value(instanceOf(Double.class)))
          .andExpect(
              jsonPath("$.score.average").value(greaterThanOrEqualTo(0.0)))
          .andExpect(
              jsonPath("$.score.average").value(lessThanOrEqualTo(100.0)));
    }

    @Test
    @DisplayName("Not matching user id with valid score returns forbidden.")
    public void not_match_user_id_returns_forbidden() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 2L;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isForbidden())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("Guest with valid score returns not authorised.")
    public void guest_returns_unauthorised() throws Exception {
      int score = 100;
      Long userId = 1L;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("When score is below 0 return bad request.")
    public void below_zero_score_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      Long userId = 1L;
      int score = -1;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect((status().isBadRequest()))
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.SCORE_POSITIVE_INT_MSG));
    }

    @Test
    @DisplayName("When score is decimal return bad request.")
    public void decimal_score_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int userId = 2;
      double score = 100.3;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect((status().isBadRequest()))
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is a decimal return bad request.")
    public void decimal_userId_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      double userId = 1.2;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When given a malformed JSON object then return bad request.")
    public void malformed_json_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      // Missing closing brace
      String json = "{\"userId\": 1, \"score\": 200";

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.JSON_PARSE_ERROR));
    }

    @Test
    @DisplayName("When score is not a number return bad request.")
    public void nan_score_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      Long userId = 1L;
      String score = "Banana";
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect((status().isBadRequest()))
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is not a number return bad request.")
    public void nan_userId_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      String userId = "banana";
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is negative return bad request.")
    public void negative_userId_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      int userId = -100;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_ID_VALUE_MSG));
    }

    @Test
    @DisplayName("When userId does not exist returns 404 not found.")
    public void non_extant_userId_returns_not_found() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      int score = 100;
      int userId = 9999;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("When score is not defined return bad request.")
    public void not_defined_score_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int userId = 1;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.SCORE_NULL_MSG));
    }

    @Test
    @DisplayName("When score is null return bad request.")
    public void null_score_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      Long userId = 1L;
      Integer score = null;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect((status().isBadRequest()))
          .andExpect(jsonPath("$.message").value(ValidationMsg.SCORE_NULL_MSG));
    }

    @Test
    @DisplayName("When userId is null return bad request.")
    public void null_userId_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      Long userId = null;
      int score = 100;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("userId", userId);
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_ID_NULL_MSG));
    }

    @Test
    @DisplayName("When userId is undefined return bad request.")
    public void undefined_userId_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Integer hits = 10;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_ID_NULL_MSG));
    }

    @Test
    @DisplayName("When hits is negative return bad request")
    public void when_hits_negative_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = -1;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.HITS_VALUE_MSG));
    }

    @Test
    @DisplayName("When hits is a decimal return bad request")
    public void when_hits_decimal_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      double hits = 1.2;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When hits is a string return bad request")
    public void when_hits_string_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      String hits = "banana";
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When hits is null return bad request")
    public void when_hits_null_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = null;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.HITS_NULL_MSG));
    }

    @Test
    @DisplayName("When hits is empty field return bad request")
    public void when_hits_empty_field_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer notHits = 20;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.HITS_NULL_MSG));
    }

    @Test
    @DisplayName("When notHits is negative return bad request")
    public void when_nothits_negative_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      Integer notHits = -1;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.NO_HITS_VALUE_MSG));
    }

    @Test
    @DisplayName("When notHits is decimal return bad request")
    public void when_nothits_decimal_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      double notHits = 1.1;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When notHits is a string return bad request")
    public void when_nothits_string_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      String notHits = "banana";
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When notHits is null return bad request")
    public void when_nothits_null_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      Integer notHits = null;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.NO_HITS_NULL_MSG));
    }

    @Test
    @DisplayName("When notHits is null return bad request")
    public void when_nothits_empty_field_returns_bad_request()
        throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      Integer misses = 2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.NO_HITS_NULL_MSG));
    }

    @Test
    @DisplayName("When misses is negative return bad request")
    public void when_misses_negative_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      Integer notHits = 10;
      Integer misses = -1;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.MISSES_VALUE_MSG));
    }

    @Test
    @DisplayName("When misses is decimal return bad request")
    public void when_misses_decimal_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      Integer notHits = 10;
      double misses = 2.2;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When misses is string return bad request")
    public void when_misses_string_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      Integer notHits = 10;
      String misses = "banana";

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When misses is null return bad request")
    public void when_misses_null_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      Integer notHits = 10;
      Integer misses = null;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);
      scoreMap.put("misses", misses);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.MISSES_NULL_MSG));
    }

    @Test
    @DisplayName("When misses is null return bad request")
    public void when_misses_not_defined_returns_bad_request() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      int score = 100;
      Long userId = 1L;
      Integer hits = 30;
      Integer notHits = 10;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("userId", userId);
      scoreMap.put("hits", hits);
      scoreMap.put("notHits", notHits);

      String json = objectMapper.writeValueAsString(scoreMap);

      mvc.perform(MockMvcRequestBuilders.post("/score/")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.MISSES_NULL_MSG));
    }
  }

  @Nested
  @Transactional
  @DisplayName("GET /score/top-ten/")
  class GetTopTenScores {
    @Test
    @DisplayName("When valid request returns top ten scores sorted.")
    public void valid_request_returns_top_ten_scores_sorted() throws Exception {
      mvc.perform(MockMvcRequestBuilders.get("/score/top-ten/"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.scores").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].id").isNotEmpty())
          .andExpect(
              jsonPath("$.scores[*].id").value(everyItem(greaterThan(0))))
          .andExpect(jsonPath("$.scores[*].score").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].score").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].score").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].username").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].username").value(
              everyItem(instanceOf(String.class))))
          .andExpect(jsonPath("$.scores[*].username").value(
              everyItem(not(emptyOrNullString()))))
          .andExpect(jsonPath("$.scores[*].hits").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].hits").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].hits").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].notHits").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].notHits").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].notHits").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].misses").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].misses").value(
              everyItem(instanceOf(Number.class))))
          .andExpect(jsonPath("$.scores[*].misses").value(
              everyItem(greaterThanOrEqualTo(0))))
          .andExpect(jsonPath("$.scores[*].average").isNotEmpty())
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(instanceOf(Double.class))))
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(greaterThanOrEqualTo(0.0))))
          .andExpect(jsonPath("$.scores[*].average").value(
              everyItem(lessThanOrEqualTo(100.0))));
    }
  }

  @Nested
  @Transactional
  @DisplayName("GET /score/avg/{userId}/")
  class GetAvgScoreByUserId {
    @Test
    @DisplayName("When valid request returns user's average score")
    public void valid_request_returns_users_avg_score() throws Exception {
      int userId = 1;

      mvc.perform(MockMvcRequestBuilders.get("/score/avg/" + userId + "/"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.score.userId").value(userId))
          .andExpect(
              jsonPath("$.score.username").value(instanceOf(String.class)))
          .andExpect(
              jsonPath("$.score.username").value(not(emptyOrNullString())))
          .andExpect(
              jsonPath("$.score.totalHits").value(instanceOf(Number.class)))
          .andExpect(
              jsonPath("$.score.totalHits").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.totalNotHits").value(instanceOf(Number.class)))
          .andExpect(
              jsonPath("$.score.totalNotHits").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.totalMisses").value(instanceOf(Number.class)))
          .andExpect(
              jsonPath("$.score.totalMisses").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.avgScore").value(instanceOf(Double.class)))
          .andExpect(
              jsonPath("$.score.avgScore").value(greaterThanOrEqualTo(0.0)))
          .andExpect(
              jsonPath("$.score.avgAccuracy").value(instanceOf(Double.class)))
          .andExpect(
              jsonPath("$.score.avgAccuracy").value(greaterThanOrEqualTo(0.0)))
          .andExpect(
              jsonPath("$.score.avgAccuracy").value(lessThanOrEqualTo(100.0)));
    }

    @Test
    @DisplayName("When userId is a string return bad request")
    public void when_user_id_is_string_return_bad_request() throws Exception {
      String userId = "banana";

      mvc.perform(MockMvcRequestBuilders.get("/score/avg/" + userId + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is decimal return bad request")
    public void when_user_id_is_decimal_return_bad_request() throws Exception {
      double userId = 1.1;

      mvc.perform(MockMvcRequestBuilders.get("/score/avg/" + userId + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is negative return bad request")
    public void when_user_id_is_negative_return_bad_request() throws Exception {
      int userId = -1;

      mvc.perform(MockMvcRequestBuilders.get("/score/avg/" + userId + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_ID_VALUE_MSG));
    }
  }

  @Nested
  @Transactional
  @DisplayName("GET /score/last/{userId}")
  class GetLastScoreByUserId {
    @Test
    @DisplayName("When valid request return user's last score")
    public void valid_request_return_users_last_score() throws Exception {
      int userId = 1;

      mvc.perform(MockMvcRequestBuilders.get("/score/last/" + userId + "/"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.score.userId").value(userId))
          .andExpect(
              jsonPath("$.score.username").value(instanceOf(String.class)))
          .andExpect(
              jsonPath("$.score.username").value(not(emptyOrNullString())))
          .andExpect(jsonPath("$.score.hits").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.hits").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.notHits").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.notHits").value(greaterThanOrEqualTo(0)))
          .andExpect(jsonPath("$.score.misses").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.misses").value(greaterThanOrEqualTo(0)))
          .andExpect(jsonPath("$.score.score").value(instanceOf(Number.class)))
          .andExpect(jsonPath("$.score.score").value(greaterThanOrEqualTo(0)))
          .andExpect(
              jsonPath("$.score.average").value(instanceOf(Double.class)))
          .andExpect(
              jsonPath("$.score.average").value(greaterThanOrEqualTo(0.0)))
          .andExpect(
              jsonPath("$.score.average").value(lessThanOrEqualTo(100.0)));
    }

    @Test
    @DisplayName("When userId is a string return bad request")
    public void when_user_id_is_string_return_bad_request() throws Exception {
      String userId = "banana";

      mvc.perform(MockMvcRequestBuilders.get("/score/last/" + userId + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is decimal return bad request")
    public void when_user_id_is_decimal_return_bad_request() throws Exception {
      double userId = 1.1;

      mvc.perform(MockMvcRequestBuilders.get("/score/last/" + userId + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.INCORRECT_PATH_VAR_DATA_TYPE));
    }

    @Test
    @DisplayName("When userId is negative return bad request")
    public void when_user_id_is_negative_return_bad_request() throws Exception {
      int userId = -1;

      mvc.perform(MockMvcRequestBuilders.get("/score/last/" + userId + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_ID_VALUE_MSG));
    }
  }
}
