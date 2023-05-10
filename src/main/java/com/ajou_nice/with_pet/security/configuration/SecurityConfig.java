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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtFilter jwtFilter;

    private final String[] GET_PERMIT_URL = {
            "/api/v1/dogs/*",
            "/api/v1/dogs"
    };
    private final String[] POST_PERMIT_URL = {
            "/api/v1/users/signup",
            "/api/v1/users/login",
            "/api/v1/dogs/register-dog",
            "/api/v1/applicate-petsitter",
            "/api/v1/users/show-applicatestate"
    };

    private static final String[] DOC_URLS = {
            "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                //서버에 위험을 끼치는 요청들을 보내는 공격을 방어하기 위해 post 요청마다 token이 필요한 과정을 생략.
                .csrf().disable()
                //CORS
                .cors().configurationSource(corsConfigurationSource()).and();

        //세션 사용 X
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //URL 관리
        http
                .authorizeRequests()
                .antMatchers(GET_PERMIT_URL).permitAll()
                .antMatchers(POST_PERMIT_URL).permitAll()
                .antMatchers(DOC_URLS).permitAll()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
