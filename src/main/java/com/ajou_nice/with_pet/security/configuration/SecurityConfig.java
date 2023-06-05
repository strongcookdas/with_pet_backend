package com.ajou_nice.with_pet.security.configuration;

import com.ajou_nice.with_pet.security.filter.JwtFilter;
import com.ajou_nice.with_pet.security.handler.AuthenticationManager;
import com.ajou_nice.with_pet.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final AuthenticationManager authenticationManager;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtFilter jwtFilter;

    private final String[] GET_PERMIT_URL = {
            "/api/v1/dogs/*",
            "/api/v1/dogs",
            "/api/v1/petsitter/*",
            "/api/v1/show-petsitter",
            "/api/v1/admin/*",
            "/api/v1/show-applicants",
            "/api/v1/show-applicant/{applicantId}",
            "/api/v1/show-services",
            "/api/v1/show-critical-services",
            "/api/v1/users/show-applicateInfo",
            "/payment/success", "http://ec2-13-209-73-128.ap-northeast-2.compute.amazonaws.com:8080/payment-cancel",
            "http://ec2-13-209-73-128.ap-northeast-2.compute.amazonaws.com:8080/payment-fail",
            "/chat/*"
    };
    private final String[] POST_PERMIT_URL = {
            "/api/v1/users/signup",
            "/api/v1/users/login",
            "/api/v1/admin/*",
            "/api/v1/users/applicate-petsitter",
            "/api/v1/file/upload",
            "/payment/ready",
            "https://kapi.kakao.com/v1/payment/ready",
            "/payment/refund",
            "/chat/room"
    };
    private final String[] PUT_PERMIT_URL = {
            "/api/v1/petsitter/*",
            "/api/v1/admin/*",
            "/api/v1/users/update-applicateInfo"
    };
    private final String[] DELETE_PERMIT_URL = {
            "/api/v1/admin/service"
    };
    private final String[] NO_TOKEN_URL = {
            "https://kapi.kakao.com/v1/payment/ready",
            "/payment/ready"
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
                .antMatchers("/ws/chat").permitAll()
                .antMatchers("/payment-cancel", "/payment-fail").permitAll()
                .antMatchers(GET_PERMIT_URL).permitAll()
                .antMatchers(POST_PERMIT_URL).permitAll()
                .antMatchers(PUT_PERMIT_URL).permitAll()
                .antMatchers(DELETE_PERMIT_URL).permitAll()
                .antMatchers(DOC_URLS).permitAll()
                .antMatchers(HttpMethod.GET).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.POST).authenticated()
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
