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

  private AdministratorService administratorService;

  public MOATSecurityConfig(AdministratorService administratoService) {
    logger.info("Constructing MoatSecurityConfig.");

    this.administratorService = administratoService;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(
        new MOATUserDetailsService(this.administratorService));
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/get-leaderboard/").permitAll()
        .antMatchers("/send-score/").permitAll()
        .anyRequest().fullyAuthenticated()
        .and()
        .httpBasic()
        .and()
        .cors()
        .and()
        .headers().frameOptions().sameOrigin()
        .httpStrictTransportSecurity().disable()
        .and()
        .csrf().disable();
  }
}
