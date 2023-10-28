package com.exam_organizer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests(auth-> auth
                        .requestMatchers("/")
                        .permitAll()
//                        .requestMatchers("/resources/**")
//                        .permitAll()
                        .requestMatchers("/signup")
                        .permitAll()

                        .anyRequest()
                        .authenticated())
                .formLogin(form->form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile")
                        .failureHandler(authenticationFailureHandler())
                        .permitAll())
                .logout(logout->
                        logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll());

        return http.build();
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return ((request, response, exception) -> {
            response.sendRedirect("/failed");
        });
    }
}
