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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
  private final static Logger logger =
      LoggerFactory.getLogger(UserControllerTest.class);

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
  @DisplayName("GET /{username}/")
  class GetUserByUserNameTests {
    @Test
    @DisplayName("When admin returns valid user.")
    public void when_admin_returns_valid_user() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      String username = "MATTD";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user.id").isNotEmpty())
          .andExpect(jsonPath("$.user.id").isNumber())
          .andExpect(jsonPath("$.user.id").value(greaterThan(0)))
          .andExpect(jsonPath("$.user.id").isNotEmpty())
          .andExpect(jsonPath("$.user.username").isNotEmpty())
          .andExpect(jsonPath("$.user.username").value(username))
          .andExpect(jsonPath("$.user.email").isNotEmpty())
          .andExpect(jsonPath("$.user.email").isString());
    }

    @Test
    @DisplayName("When user is the requested user return valid user.")
    public void when_user_is_requested_user_return_valid_user()
        throws Exception {
      String username = "MATTD";

      String token = getJwtToken(username, "passw");

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user.id").isNotEmpty())
          .andExpect(jsonPath("$.user.id").isNumber())
          .andExpect(jsonPath("$.user.id").value(greaterThan(0)))
          .andExpect(jsonPath("$.user.id").isNotEmpty())
          .andExpect(jsonPath("$.user.username").isNotEmpty())
          .andExpect(jsonPath("$.user.username").value(username))
          .andExpect(jsonPath("$.user.email").isNotEmpty())
          .andExpect(jsonPath("$.user.email").isString());
    }

    @Test
    @DisplayName("When user is not requested user return forbidden.")
    public void when_user_is_not_requested_user_return_forbidden()
        throws Exception {
      String username = "MATTD";

      String token = getJwtToken(username, "passw");

      mvc.perform(MockMvcRequestBuilders.get("/user/BOBBY/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isForbidden())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("When user is not logged in return unauthorised.")
    public void when_user_is_not_logged_in_return_unauthorised()
        throws Exception {
      String username = "MATTD";

      mvc.perform(MockMvcRequestBuilders.get("/user/BOBBY/"))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("When user doesn't exist return 404 not found.")
    public void user_doesnt_exist_return_not_found() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      String username = "NOUSER";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName(
        "When username contains invalid characters return 400 bad request.")
    public void username_contains_invalid_characters_return_bad_request()
        throws Exception {
      String token = getJwtToken("ADMIN", "password");

      String username = "7*&*2";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }

    @Test
    @DisplayName("When username is too short return 400 bad request.")
    public void username_is_too_short_return_bad_request() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      String username = "W";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_LENGTH_MSG));
    }

    @Test
    @DisplayName("When username is too long return 400 bad request.")
    public void username_is_too_long_return_bad_request() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      String username = "TOOOOOOOOOOOOOOLOOOOOOOOOOOOOOOOOOOOOOOOOOONG";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_LENGTH_MSG));
    }

    @Test
    @DisplayName(
        "When username contains lowercase characters return 400 bad request.")
    public void username_has_lower_case_chars_return_bad_request()
        throws Exception {
      String token = getJwtToken("ADMIN", "password");

      String username = "WsdsdsdW";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }
  }

  @Nested
  @Transactional
  @DisplayName("GET /user/")
  class GetUsersTests {
    @Test
    @DisplayName("Admin request returns all users.")
    public void admin_request_returns_all_users() throws Exception {
      String token = getJwtToken("ADMIN", "password");

      mvc.perform(MockMvcRequestBuilders.get("/user/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.users").exists())
          .andExpect(jsonPath("$.users").isArray())
          .andExpect(jsonPath("$.users").isNotEmpty())
          .andExpect(jsonPath("$.users[*].password").doesNotExist())
          .andExpect(jsonPath("$.users[*].id").exists())
          .andExpect(
              jsonPath("$.users[*].id").value(everyItem(isA(Number.class))))
          .andExpect(jsonPath("$.users[*].id").value(everyItem(greaterThan(0))))
          .andExpect(jsonPath("$.users[*].username").exists())
          .andExpect(jsonPath("$.users[*].username").value(
              everyItem(isA(String.class))))
          .andExpect(jsonPath("$.users[*].username").value(
              everyItem(is(not(emptyOrNullString())))))
          .andExpect(jsonPath("$.users[*].email").exists())
          .andExpect(
              jsonPath("$.users[*].email").value(everyItem(isA(String.class))))
          .andExpect(jsonPath("$.users[*].email").value(
              everyItem(is(not(emptyOrNullString())))));
    }

    @Test
    @DisplayName("User request returns not authorized.")
    public void user_request_returns_not_authorized() throws Exception {
      String token = getJwtToken("MATTD", "passw");

      mvc.perform(MockMvcRequestBuilders.get("/user/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isForbidden())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("Guest returns not authorized.")
    public void guest_request_returns_not_authorized() throws Exception {
      mvc.perform(MockMvcRequestBuilders.get("/user/"))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }
  }

  @Nested
  @Transactional
  @DisplayName("POST /user/")
  class PostUserTests {
    @Test
    @DisplayName("Valid request creates a users.")
    public void valid_request_creates_a_user() throws Exception {
      String email = "newuser@email.com";
      String username = "NEWUSER";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user.password").doesNotExist())
          .andExpect(jsonPath("$.user").isNotEmpty())
          .andExpect(jsonPath("$.user.id").isNumber())
          .andExpect(jsonPath("$.user.id").value(greaterThan(0)))
          .andExpect(jsonPath("$.user.username").value(username))
          .andExpect(jsonPath("$.user.email").value(email));
    }

    @Test
    @DisplayName("When username exists return bad request.")
    public void when_username_exists_return_bad_request() throws Exception {
      String email = "newuser@email.com";
      String username = "MATTD";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("When email already exists return bad request.")
    public void when_email_exists_return_bad_request() throws Exception {
      String email = "matt@email.com";
      String username = "NEWUSER";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("When email field doesn't exist return bad request.")
    public void when_email_field_not_exist_return_bad_request()
        throws Exception {
      String username = "NEWUSER";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.EMAIL_NULL_MSG));
    }

    @Test
    @DisplayName("When email field is null return bad request.")
    public void when_email_is_null_return_bad_request() throws Exception {
      String email = null;
      String username = "NEWUSER";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("username", username);
      userMap.put("password", password);
      userMap.put("email", email);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.EMAIL_NULL_MSG));
    }

    @Test
    @DisplayName("When email is too short return bad request.")
    public void when_email_is_too_short_return_bad_request() throws Exception {
      String email = "n@s";
      String username = "NEWUSER";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_LENGTH_MSG));
    }

    @Test
    @DisplayName("When email is too long return bad request.")
    public void when_email_is_too_long_return_bad_request() throws Exception {
      String email =
          "n@ssdasdasdassssssssssssssssssssssssssssssssssssssssssssssssssssss";
      String username = "NEWUSER";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_LENGTH_MSG));
    }

    @Test
    @DisplayName("When email contains white space return bad request.")
    public void when_email_has_white_space_return_bad_request()
        throws Exception {
      String email = "matt@email.com ";
      String username = "NEWUSER";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_PATTERN_MSG));
    }

    @Test
    @DisplayName("When email contains invalid characters return bad request.")
    public void when_email_has_invalid_chars_return_bad_request()
        throws Exception {
      String email = "matt££%@email.com";
      String username = "NEWUSER";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_PATTERN_MSG));
    }

    @Test
    @DisplayName("When username field is empty return bad request.")
    public void when_username_field_is_empty_return_bad_request()
        throws Exception {
      String email = "valid@email.com";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_NULL_MSG));
    }

    @Test
    @DisplayName("When username is null return bad request.")
    public void when_username_is_null_return_bad_request() throws Exception {
      String username = null;
      String email = "valid@email.com";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("username", username);
      userMap.put("email", email);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_NULL_MSG));
    }

    @Test
    @DisplayName("When username is too short return bad request.")
    public void when_username_is_too_short_return_bad_request()
        throws Exception {
      String username = "N";
      String email = "validemail@email.com";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_LENGTH_MSG));
    }

    @Test
    @DisplayName("When username is too long return bad request.")
    public void when_username_is_too_long_return_bad_request()
        throws Exception {
      String email = "valid@email.com";
      String username = "NASDASDDSAADSASDSADASDASDSDAASDSDAADSASDSA";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_LENGTH_MSG));
    }

    @Test
    @DisplayName("When username is in lowercase return bad request.")
    public void when_username_is_lower_case_return_bad_request()
        throws Exception {
      String email = "valid@email.com";
      String username = "usermatt";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }

    @Test
    @DisplayName("When username contains invalid symbols return bad request.")
    public void when_username_has_invalid_symbols_return_bad_request()
        throws Exception {
      String email = "valid@email.com";
      String username = "MAS&^D";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }

    @Test
    @DisplayName("When username contains spaces return bad request.")
    public void when_username_has_spaces_return_bad_request() throws Exception {
      String email = "valid@email.com";
      String username = "MATT D";
      String password = "mypassword";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }

    @Test
    @DisplayName("When password field is empty return bad request.")
    public void when_password_field_is_empty_return_bad_request()
        throws Exception {
      String email = "valid@email.com";
      String username = "NEWUSER";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_NULL_MSG));
    }

    @Test
    @DisplayName("When password is null return bad request.")
    public void when_password_is_null_return_bad_request() throws Exception {
      String username = "NEWUSER";
      String email = "valid@email.com";
      String password = null;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_NULL_MSG));
    }

    @Test
    @DisplayName("When password is too short return bad request.")
    public void when_password_is_too_short_return_bad_request()
        throws Exception {
      String username = "NEWUSER";
      String email = "valid@email.com";
      String password = "p";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_LENGTH_MSG));
    }

    @Test
    @DisplayName("When password is too long return bad request.")
    public void when_password_is_too_long_return_bad_request()
        throws Exception {
      String username = "NEWUSER";
      String email = "valid@email.com";
      String password = "pasdasdasdadsasdasdasdasdasdasdadasdasdadasdadsa";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_LENGTH_MSG));
    }

    @Test
    @DisplayName("When password contains spaces return bad request.")
    public void when_password_has_spaces_return_bad_request() throws Exception {
      String username = "NEWUSER";
      String email = "valid@email.com";
      String password = "paasdsds ";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_PATTERN_MSG));
    }

    @Test
    @DisplayName(
        "When password contains invalid characters return bad request.")
    public void when_password_has_invalid_chars_return_bad_request()
        throws Exception {
      String username = "NEWUSER";
      String email = "valid@email.com";
      String password = "paas`dsds";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("email", email);
      userMap.put("username", username);
      userMap.put("password", password);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.post("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_PATTERN_MSG));
    }
  }

  @Nested
  @Transactional
  @DisplayName("PATCH /user/")
  class PatchUserDetails {
    @Test
    @DisplayName("When valid new email returns OK.")
    public void when_valid_new_email_returns_ok() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "mynewemail@email.com";
      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", username);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user").isNotEmpty())
          .andExpect(jsonPath("$.user.id").value(userId))
          .andExpect(jsonPath("$.user.username").value(username))
          .andExpect(jsonPath("$.user.email").value(newEmail));
    }

    @Test
    @DisplayName("When valid new username returns OK.")
    public void when_valid_new_username_returns_ok() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String email = "mattd@email.com";
      String newUsername = "MATTYD";
      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", email);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user").isNotEmpty())
          .andExpect(jsonPath("$.user.id").value(userId))
          .andExpect(jsonPath("$.user.username").value(newUsername))
          .andExpect(jsonPath("$.user.email").value(email));
    }

    @Test
    @DisplayName("When incorrect userId return forbidden.")
    public void when_incorrect_userid_return_forbidden() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 2;
      String email = "mattd@email.com";
      String newUsername = "MATTYD";
      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", email);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isForbidden())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("When admin can update any user return OK.")
    public void when_admin_can_update_any_user_return_ok() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      int userId = 2;
      String newEmail = "randomemail@email.com";
      String newUsername = "NEWUSERNAME";
      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user").isNotEmpty())
          .andExpect(jsonPath("$.user.id").value(userId))
          .andExpect(jsonPath("$.user.username").value(newUsername))
          .andExpect(jsonPath("$.user.email").value(newEmail));
    }

    @Test
    @DisplayName("When guest return unauthorized.")
    public void when_guest_return_unauthorized() throws Exception {
      int userId = 1;
      String newEmail = "randomemail@email.com";
      String newUsername = "NEWUSERNAME";
      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("Invalid new email return bad request.")
    public void when_invalid_new_email_return_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "invalid";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", username);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_PATTERN_MSG));
    }

    @Test
    @DisplayName("When email too short return bad request.")
    public void when_email_too_short_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "m@m";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", username);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_LENGTH_MSG));
    }

    @Test
    @DisplayName("When email too long return bad request.")
    public void when_email_too_long_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail =
          "m@tooooooooooooooooooooooooooooooooooooooooooooolong.com";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", username);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_LENGTH_MSG));
    }

    @Test
    @DisplayName("When email is null return bad request")
    public void when_email_null_return_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = null;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", username);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.EMAIL_NULL_MSG));
    }

    @Test
    @DisplayName(
        "When new email already belongs to another user return bad request")
    public void when_new_email_belongs_to_another_user_return_bad_request()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "bobby@email.com";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", username);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.EMAIL_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("Invalid new username return bad request.")
    public void when_invalid_new_username_return_bad_request()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "valid@valid.com";
      String newUsername = "invalid";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }

    @Test
    @DisplayName("When username is too short return bad request.")
    public void when_new_username_too_short_return_bad_request()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "valid@valid.com";
      String newUsername = "SD";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_LENGTH_MSG));
    }

    @Test
    @DisplayName("When username is too long return bad request.")
    public void when_new_username_too_long_return_bad_request()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "valid@valid.com";
      String newUsername = "SDASDASDASDASDDASSADSDASDAASDASDASDASDDASSD";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_LENGTH_MSG));
    }

    @Test
    @DisplayName("When username is null return bad request.")
    public void when_new_username_null_return_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "valid@valid.com";
      String newUsername = null;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_NULL_MSG));
    }

    @Test
    @DisplayName("When username is lowercase return bad request.")
    public void when_new_username_lowercase_return_bad_request()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "valid@valid.com";
      String newUsername = "lowercasename";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }

    @Test
    @DisplayName("When username belongs to another user return bad request.")
    public void when_new_username_belongs_to_another_user_return_bad_request()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newEmail = "valid@valid.com";
      String newUsername = "BOBBY";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(
              ValidationMsg.USERNAME_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("When admin invalid id return bad request")
    public void when_admin_invalid_id_return_bad_request() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      int userId = -1;
      String newEmail = "valid@valid.com";
      String newUsername = "VALIDNICK";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.ID_VALUE_MSG));
    }

    @Test
    @DisplayName("When admin id is a decimal return bad request")
    public void when_admin_id_is_decimal_return_bad_request() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      double userId = 1.1;
      String newEmail = "valid@valid.com";
      String newUsername = "VALIDNICK";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When admin id is a string return bad request")
    public void when_admin_id_is_a_string_return_bad_request()
        throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      String userId = "banana";
      String newEmail = "valid@valid.com";
      String newUsername = "VALIDNICK";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("When admin and id is non extant return not found.")
    public void when_admin_id_is_non_extant_return_not_found()
        throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      int userId = 9999;
      String newEmail = "valid@valid.com";
      String newUsername = "VALIDNICK";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("username", newUsername);
      userMap.put("email", newEmail);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("When request body is null return bad request.")
    public void when_request_body_is_null_return_bad_request()
        throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      mvc.perform(MockMvcRequestBuilders.patch("/user/")
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.JSON_PARSE_ERROR));
    }
  }

  @Nested
  @Transactional
  @DisplayName("PATCH /user/password/")
  class PatchUserPassword {
    @Test
    @DisplayName("Logged in user provides valid password returns OK.")
    public void when_user_provides_valid_password_returns_ok()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newPassword = "newpass";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", newPassword);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user").isNotEmpty())
          .andExpect(jsonPath("$.user.id").value(userId))
          .andExpect(jsonPath("$.user.username").value(username))
          .andExpect(jsonPath("$.user.email").value(not(emptyOrNullString())));

      Map<String, Object> authMap = new HashMap<>();
      authMap.put("username", username);
      authMap.put("password", newPassword);

      String authJson = objectMapper.writeValueAsString(authMap);

      // Verify password has changed
      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(authJson))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user.id").value(userId));
    }

    @Test
    @DisplayName("Password is a number returns OK.")
    public void password_is_number_returns_OK() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      int newPassword = 232132123;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", newPassword);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user").isNotEmpty())
          .andExpect(jsonPath("$.user.id").value(userId))
          .andExpect(jsonPath("$.user.username").value(username))
          .andExpect(jsonPath("$.user.email").value(not(emptyOrNullString())));
    }

    @Test
    @DisplayName("Admin can change any user's password.")
    public void admin_changes_user_password_returns_ok() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      int userId = 1;
      String userUsername = "MATTD";
      String newPassword = "newpass";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", newPassword);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user").isNotEmpty())
          .andExpect(jsonPath("$.user.id").value(userId))
          .andExpect(jsonPath("$.user.username").value(userUsername))
          .andExpect(jsonPath("$.user.email").value(not(emptyOrNullString())));

      Map<String, Object> authMap = new HashMap<>();
      authMap.put("username", userUsername);
      authMap.put("password", newPassword);

      String authJson = objectMapper.writeValueAsString(authMap);

      // Verify password has changed
      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(authJson))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.user.id").value(userId));
    }

    @Test
    @DisplayName(
        "User cannot change other user's password and returns forbidden.")
    public void user_cannot_change_other_users_password_returns_forbidden()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 2;
      String newPassword = "newpass";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", newPassword);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isForbidden())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.ERROR_UNAUTHORISED));
    }

    @Test
    @DisplayName("Password too short returns bad request.")
    public void password_too_short_returns_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newPassword = "pa";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", newPassword);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_LENGTH_MSG));
    }

    @Test
    @DisplayName("Password too long returns bad request.")
    public void password_too_long_returns_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newPassword = "tooooollllllllllllllllllooooooooooooooooooooooooog";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", newPassword);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_LENGTH_MSG));
    }

    @Test
    @DisplayName("Password contains invalid characters returns bad request.")
    public void password_has_invalid_chars_returns_bad_request()
        throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newPassword = "too og";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", newPassword);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_PATTERN_MSG));
    }

    @Test
    @DisplayName("Password is null returns bad request.")
    public void password_is_null_returns_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;
      String newPassword = null;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", newPassword);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_NULL_MSG));
    }

    @Test
    @DisplayName("Password is empty field returns bad request.")
    public void password_is_empty_field_returns_bad_request() throws Exception {
      String username = "MATTD";
      String password = "passw";

      String token = getJwtToken(username, password);

      int userId = 1;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.PASSWORD_NULL_MSG));
    }

    @Test
    @DisplayName("Id is non extant returns not found.")
    public void id_non_extant_returns_not_found() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      int userId = 99999;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", "newpass");

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("Id is a string returns bad request.")
    public void id_is_a_string_returns_bad_request() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      String userId = "banana";

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", "newpass");

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("Id is decimal returns bad request.")
    public void id_is_a_decimal_returns_bad_request() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      double userId = 2.2;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", "newpass");

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INCORRECT_DATA_TYPE));
    }

    @Test
    @DisplayName("Id is decimal returns bad request.")
    public void id_is_negative_returns_bad_request() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      int userId = -2;

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", userId);
      userMap.put("password", "newpass");

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.ID_VALUE_MSG));
    }

    @Test
    @DisplayName("Id is null returns bad request.")
    public void id_is_null_returns_bad_request() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", null);
      userMap.put("password", "newpass");

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.ID_NULL_MSG));
    }

    @Test
    @DisplayName("Id is empty field returns bad request.")
    public void id_is_empty_field_returns_bad_request() throws Exception {
      String username = "ADMIN";
      String password = "password";

      String token = getJwtToken(username, password);

      Map<String, Object> userMap = new HashMap<>();
      userMap.put("password", "newpass");

      String json = objectMapper.writeValueAsString(userMap);

      mvc.perform(MockMvcRequestBuilders.patch("/user/password/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .header("Authorization", "Bearer " + token))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(ValidationMsg.ID_NULL_MSG));
    }
  }
}

