package com.moat.config;

import com.moat.security.handler.JwtAuthenticationEntryPoint;
import com.moat.jwt.JwtRequestFilter;
import com.moat.security.MOATUserDetailsService;
import com.moat.security.handler.CustomAccessDeniedHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

  private final MOATUserDetailsService moatUserDetailsService;
  private final PasswordEncoder passwordEncoder;

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtRequestFilter jwtRequestFilter;

  private final AccessDeniedHandler customAccessDeniedHandler;

  public SecurityConfig(MOATUserDetailsService moatUserDetailsService,
      PasswordEncoder passwordEncoder,
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      JwtRequestFilter jwtRequestFilter,
      CustomAccessDeniedHandler customAccessDeniedHandler) {
    logger.debug("Constructing MoatSecurityConfig.");

    this.moatUserDetailsService = moatUserDetailsService;
    this.passwordEncoder = passwordEncoder;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtRequestFilter = jwtRequestFilter;
    this.customAccessDeniedHandler = customAccessDeniedHandler;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    logger.debug("In configure(AuthenticationManagerBuilder) in " +
        "MOATSecurityConfig.");
    auth.userDetailsService(moatUserDetailsService)
        .passwordEncoder(passwordEncoder);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    logger.debug("In configure(HttpSecurity) in MOATSecurityConfig.");

    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/", "/authenticate/")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/score/")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/score/")
        .hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/score/")
        .hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/score/*/", "/score/avg/*/",
            "/score/last/*/")
        .permitAll()
        .antMatchers(HttpMethod.DELETE, "/score/*/")
        .hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/user/")
        .hasRole("ADMIN")
        .antMatchers(HttpMethod.POST, "/user/")
        .permitAll()
        .antMatchers(HttpMethod.PATCH, "/user/")
        .hasRole("USER")
        .antMatchers(HttpMethod.GET, "/user/*/")
        .hasRole("USER")
        .anyRequest()
        .fullyAuthenticated()
        .and()
        .cors()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(customAccessDeniedHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(jwtRequestFilter,
        UsernamePasswordAuthenticationFilter.class);
  }
}

