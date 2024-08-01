package com.moat.config;

import com.moat.jwt.JwtAuthenticationEntryPoint;
import com.moat.jwt.JwtRequestFilter;
import com.moat.security.MOATAdminDetailsService;
import com.moat.security.MOATUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

  private final MOATUserDetailsService moatUserDetailsService;
  private final MOATAdminDetailsService moatAdminDetailsService;
  private final PasswordEncoder passwordEncoder;

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtRequestFilter jwtRequestFilter;

  public SecurityConfig(MOATUserDetailsService moatUserDetailsService,
      MOATAdminDetailsService moatAdminDetailsService,
      PasswordEncoder passwordEncoder,
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      JwtRequestFilter jwtRequestFilter) {
    logger.debug("Constructing MoatSecurityConfig.");

    this.moatUserDetailsService = moatUserDetailsService;
    this.moatAdminDetailsService = moatAdminDetailsService;
    this.passwordEncoder = passwordEncoder;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    logger.debug("In configure(AuthenticationManagerBuilder) in " +
        "MOATSecurityConfig.");
    auth.userDetailsService(moatUserDetailsService)
        .passwordEncoder(passwordEncoder);
    auth.userDetailsService(moatAdminDetailsService)
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
        .antMatchers("/score/")
        .permitAll()
        .antMatchers("/score/*/")
        .permitAll()
        .antMatchers("/user/")
        .permitAll()
        .antMatchers("/user/*/")
        .permitAll()
        .antMatchers("/admin/**")
        .hasRole("ADMIN")
        .anyRequest()
        .fullyAuthenticated()
        .and()
        .cors()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(jwtRequestFilter,
        UsernamePasswordAuthenticationFilter.class);
  }
}

