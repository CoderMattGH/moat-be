package com.moat.security.handler;

import com.moat.constant.ValidationMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private static final Logger logger =
      LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    logger.debug("In commence() in JwtAuthenticationEntryPoint.");

    String json = String.format("{\"message\" : \"%s\"}",
        ValidationMsg.ERROR_UNAUTHORISED);

    PrintWriter out = response.getWriter();
    response.setStatus(401);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    out.print(json);
    out.flush();
  }
}