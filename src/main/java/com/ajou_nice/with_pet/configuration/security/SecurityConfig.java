package com.ajou_nice.with_pet.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    private final String[] PERMIT_URL = {
            "api/v1/users/signup",
            "api/v1/users/login"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //서버에 위험을 끼치는 요청들을 보내는 공격을 방어하기 위해 post 요청마다 token이 필요한 과정을 생략.
                .csrf().disable()
                //CORS
                .cors().and();
//                .authorizeRequests()
//                .antMatchers(PERMIT_URL).permitAll();
        return http.build();
    }
}
