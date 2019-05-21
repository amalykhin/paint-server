package com.github.amalykhin.paint.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    SimpleUrlAuthenticationSuccessHandler successHandler;
    //@Autowired
    //SimpleUrlAuthenticationFailureHandler failureHandler;

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password("pass").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .csrf().disable()
            .authorizeRequests()
            //.anyRequest().authenticated()
            .antMatchers("/api/**").authenticated()
            .anyRequest().permitAll()
            .and()
            /*
            .formLogin()
            .successHandler(successHandler)
    //        .failureHandler(failureHandler)
            .and()
            */
            .logout();
    }
}
