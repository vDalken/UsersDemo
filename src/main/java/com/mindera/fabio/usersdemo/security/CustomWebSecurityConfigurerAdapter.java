package com.mindera.fabio.usersdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter{

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication() //authentication is configured directly within the application's code
                .withUser("username") //username of the user should be username
                .password("{noop}password") //password of the user should be a plain string saying password
                .roles("user"); //categorizing the user based on their permission
    }

}
