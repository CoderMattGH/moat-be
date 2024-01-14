package com.moat.security;

import com.moat.entity.Administrator;
import com.moat.service.AdministratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MOATUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(MOATUserDetailsService.class);

    private final AdministratorService administratorService;

    public MOATUserDetailsService(AdministratorService administratorService) {
        logger.info("Constructing MOATUserDetailsService.");

        this.administratorService = administratorService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            final Administrator administrator = this.administratorService.findByUsername(username);

            if (administrator != null) {
                String password = administrator.getPassword();

                return User.withUsername(username).password(password).roles("ADMIN", "USER")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new UsernameNotFoundException(username);
    }
}