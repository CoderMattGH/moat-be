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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticateControllerTest {
  private final static Logger logger =
      LoggerFactory.getLogger(ScoreControllerTest.class);

  @Autowired
  private MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @Nested
  @Transactional
  @DisplayName("POST /authenticate/")
  class CreateAuthenticationToken {
    @Test
    @DisplayName("Valid user login returns valid token.")
    public void valid_user_login_returns_token() throws Exception {
      String username = "MATTD";
      String password = "passw";

      Map<String, Object> user = new HashMap<>();
      user.put("username", username);
      user.put("password", password);

      String json = objectMapper.writeValueAsString(user);

      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .content(json)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.token").isString())
          .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Incorrect user password returns unauthorised.")
    public void incorrect_user_password_returns_unauthorised()
        throws Exception {
      String username = "MATTD";
      String password = "wrongpassword";

      Map<String, Object> user = new HashMap<>();
      user.put("username", username);
      user.put("password", password);

      String json = objectMapper.writeValueAsString(user);

      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .content(json)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value((ValidationMsg.INVALID_CREDENTIALS)));
    }

    @Test
    @DisplayName("Non extant username returns unauthorised.")
    public void non_extant_username_returns_unauthorised() throws Exception {
      String username = "NOUSER";
      String password = "passw";

      Map<String, Object> user = new HashMap<>();
      user.put("username", username);
      user.put("password", password);

      String json = objectMapper.writeValueAsString(user);

      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .content(json)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value((ValidationMsg.INVALID_CREDENTIALS)));
    }

    @Test
    @DisplayName("Valid admin login returns token.")
    public void valid_admin_returns_token() throws Exception {
      String username = "ADMIN";
      String password = "password";

      Map<String, Object> user = new HashMap<>();
      user.put("username", username);
      user.put("password", password);

      String json = objectMapper.writeValueAsString(user);

      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .content(json)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.token").isNotEmpty())
          .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @DisplayName("Incorrect admin password returns unauthorised.")
    public void incorrect_admin_password_returns_unauthorised()
        throws Exception {
      String username = "ADMIN";
      String password = "wrongpass";

      Map<String, Object> user = new HashMap<>();
      user.put("username", username);
      user.put("password", password);

      String json = objectMapper.writeValueAsString(user);

      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .content(json)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INVALID_CREDENTIALS));
    }

    @Test
    @DisplayName("Null username returns unauthorised.")
    public void null_username_returns_unauthorised() throws Exception {
      String username = null;
      String password = "wrongpass";

      Map<String, Object> user = new HashMap<>();
      user.put("username", username);
      user.put("password", password);

      String json = objectMapper.writeValueAsString(user);

      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .content(json)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INVALID_CREDENTIALS));
    }

    @Test
    @DisplayName("Null password returns unauthorised.")
    public void null_password_returns_unauthorised() throws Exception {
      String username = "MATTD";
      String password = null;

      Map<String, Object> user = new HashMap<>();
      user.put("username", username);
      user.put("password", password);

      String json = objectMapper.writeValueAsString(user);

      mvc.perform(MockMvcRequestBuilders.post("/authenticate/")
              .content(json)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isUnauthorized())
          .andExpect(
              jsonPath("$.message").value(ValidationMsg.INVALID_CREDENTIALS));
    }
  }
}
