package com.lazlob.cityviewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Clock;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
public class AppConfig {

    private static final int BCRYPT_STRENGTH = 10;

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .anyRequest().permitAll()) // TODO .authenticated()
                .cors().disable()
                .csrf().disable()
                .headers(customizer -> customizer.frameOptions().sameOrigin())
                .build();
    }
}
