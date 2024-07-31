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

  @Nested
  @Transactional
  @DisplayName("GET /{username}")
  class getUserByUserNameTests {
    @Test
    @DisplayName("Successfully returns valid user.")
    public void valid_request_returns_valid_user() throws Exception {
      String username = "MATTD";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/"))
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
    @DisplayName("When user doesn't exist return 404 not found.")
    public void user_doesnt_exist_return_not_found() throws Exception {
      String username = "NOUSER";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/"))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName(
        "When username contains invalid characters return 400 bad request.")
    public void username_contains_invalid_characters_return_bad_request()
        throws Exception {
      String username = "7*&*2";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }

    @Test
    @DisplayName("When username is too short return 400 bad request.")
    public void username_is_too_short_return_bad_request() throws Exception {
      String username = "W";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_LENGTH_MSG));
    }

    @Test
    @DisplayName("When username is too long return 400 bad request.")
    public void username_is_too_long_return_bad_request() throws Exception {
      String username = "TOOOOOOOOOOOOOOLOOOOOOOOOOOOOOOOOOOOOOOOOOONG";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_LENGTH_MSG));
    }

    @Test
    @DisplayName(
        "When username contains lowercase characters return 400 bad request.")
    public void username_has_lower_case_chars_return_bad_request()
        throws Exception {
      String username = "WsdsdsdW";

      mvc.perform(MockMvcRequestBuilders.get("/user/" + username + "/"))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.USERNAME_PATTERN_MSG));
    }
  }

  @Nested
  @Transactional
  @DisplayName("GET /user/")
  class getUsersTests {
    @Test
    @DisplayName("Valid request returns all users.")
    public void valid_request_returns_all_users() throws Exception {
      mvc.perform(MockMvcRequestBuilders.get("/user/"))
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
  }

  @Nested
  @Transactional
  @DisplayName("POST /user/")
  class postUserTests {
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
}
