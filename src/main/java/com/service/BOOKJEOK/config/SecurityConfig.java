package com.service.BOOKJEOK.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable);
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //여기까지 jwt서버 기본 설정
        http.formLogin(login -> login.disable());
        http.httpBasic(basic -> basic.disable());
        http.authorizeHttpRequests(authorize ->
                authorize//.requestMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                        //.requestMatchers("/api/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        //.requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll()
        );

        return http.build();
    }
}
