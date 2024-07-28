package com.moat.config;

import com.moat.security.MOATUserDetailsService;
import com.moat.service.AdministratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class MOATSecurityConfig extends WebSecurityConfigurerAdapter {
  private Logger logger = LoggerFactory.getLogger(MOATSecurityConfig.class);

  private final AdministratorService administratorService;

  public MOATSecurityConfig(AdministratorService administratorService) {
    logger.info("Constructing MoatSecurityConfig.");

    this.administratorService = administratorService;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    logger.info("In configure(AuthenticationManagerBuilder) in " +
        "MOATSecurityConfig.");

    auth.userDetailsService(
        new MOATUserDetailsService(this.administratorService));
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    logger.info("In configure(HttpSecurity) in MOATSecurityConfig.");

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/score/")
        .permitAll()
        .antMatchers("/user/")
        .permitAll()
        .antMatchers("/")
        .permitAll()
        .anyRequest()
        .fullyAuthenticated()
        .and()
        .httpBasic()
        .and()
        .cors()
        .and()
        .headers()
        .frameOptions()
        .sameOrigin()
        .httpStrictTransportSecurity()
        .disable()
        .and()
        .csrf()
        .disable();
  }
}
