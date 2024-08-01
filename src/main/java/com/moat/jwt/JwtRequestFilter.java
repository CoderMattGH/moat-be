package com.moat.jwt;

import com.moat.constant.ValidationMsg;
import com.moat.exception.JwtParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  private final static Logger logger =
      LoggerFactory.getLogger(JwtRequestFilter.class);

  private final UserDetailsService moatUserDetailsService;
  private final JwtUtil jwtUtil;

  public JwtRequestFilter(JwtUtil jwtUtil,
      UserDetailsService moatUserDetailsService) {
    logger.debug("Constructing JwtRequestFilter.");

    this.jwtUtil = jwtUtil;
    this.moatUserDetailsService = moatUserDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain) throws IOException {
    logger.debug("In doFilterInternal() in JwtRequestFilter.");

    final String authorizationHeader = request.getHeader("Authorization");

    String username = null;
    String jwt = null;
    if (authorizationHeader != null &&
        authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      username = jwtUtil.extractUsername(jwt);
    }

    if (jwt != null &&
        SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails =
          this.moatUserDetailsService.loadUserByUsername(username);

      if (jwtUtil.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken
            usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        usernamePasswordAuthenticationToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext()
            .setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    try {
      chain.doFilter(request, response);
    } catch (ServletException e) {
      // Send back response?
      logger.error("Servlet exception!");
    }
  }
}
