package com.exam_organizer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class GlobalConfig {

    @Autowired
    @Qualifier("examOrganizerDetailService")
    private UserDetailsService examUserDetailsService;


    @Autowired
    @Qualifier("candidateDetailService")
    private UserDetailsService candidateDetailService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception{

        http.securityMatcher("/admin/**")
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/admin/signup").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form->form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/profile")
                        .failureHandler(authenticationFailureHandler())
                        .permitAll())
                .logout(logout->
                        logout.logoutUrl("/admin/logout")
                                .logoutSuccessUrl("/admin/login")
                                .permitAll())
                .authenticationProvider(daoAuthenticationProvider(examUserDetailsService));

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return ((request, response, exception) -> {
            response.sendRedirect("/failed");
        });
    }

    @Bean
    public SecurityFilterChain candidateSecurityFilterChain(HttpSecurity http) throws Exception{

        http.securityMatcher("/candidate/**")
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/candidate/signup")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(form->form
                        .loginPage("/candidate/login")
                        .defaultSuccessUrl("/candidate/home")
                        .failureHandler(candidateAuthenticationFailureHandler())
                        .permitAll())
                .logout(logout->
                        logout.logoutUrl("/candidate/logout")
                                .logoutSuccessUrl("/candidate/login")
                                .permitAll())
                .authenticationProvider(daoAuthenticationProvider(candidateDetailService));

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler candidateAuthenticationFailureHandler() {
        return ((request, response, exception) -> {
            response.sendRedirect("/failed");
        });
    }



}
