package com.ajou_nice.with_pet.security.configuration;

import com.ajou_nice.with_pet.security.filter.JwtFilter;
import com.ajou_nice.with_pet.security.handler.AuthenticationManager;
import com.ajou_nice.with_pet.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtFilter jwtFilter;
    private final String[] PERMIT_URL = {
            "/swagger-ui/**",
            "api/v1/users/signup",
            "api/v1/users/login"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                //서버에 위험을 끼치는 요청들을 보내는 공격을 방어하기 위해 post 요청마다 token이 필요한 과정을 생략.
                .csrf().disable()
                //CORS
                .cors().and();

        //세션 사용 X
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //URL 관리
        http
                .authorizeRequests()
                .antMatchers(PERMIT_URL).permitAll()
                .antMatchers(HttpMethod.GET).authenticated()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated();

        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationManager)
                .accessDeniedHandler(accessDeniedHandler);

        // jwtFilter 먼저 적용
        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
