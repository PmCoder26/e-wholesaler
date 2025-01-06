package com.parimal.e_wholesaler.user_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
