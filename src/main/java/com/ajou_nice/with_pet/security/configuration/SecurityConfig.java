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
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtFilter jwtFilter;

    private final String[] GET_PERMIT_URL = {
            "/api/v1/petsitter/*",
            "/api/v1/show-petsitter",
            "/api/v1/reservation",
            "/payment/success",
            "http://ec2-13-209-73-128.ap-northeast-2.compute.amazonaws.com:8080/payment-cancel",
            "http://ec2-13-209-73-128.ap-northeast-2.compute.amazonaws.com:8080/payment-fail",
            "/chat/*"
    };
    private final String[] POST_PERMIT_URL = {
            "/api/v1/users/signup",
            "/api/v1/users/login",
            "/api/v1/file/upload",
            "/payment/ready",
            "https://kapi.kakao.com/v1/payment/ready",
            "/payment/refund",
            "/chat/room"
    };

    private final String[] ADMIN_GET_API = {
            "/api/v1/show-applicants",
            "/api/v1/show-applicant/*",
            "/api/v1/show-services",
            "/api/v1/show-criticalservices"
    };

    private final String[] PETSITTER_APPLICANT_GET_API = {
            "/api/v1/users/show-applicateInfo",
    };
    private final String[] APPLICANT_PUT_API = {
            "/api/v1/users/update-applicateInfo"
    };
    private final String[] USER_API = {
            "/api/v1/calendar",
            "/api/v1/dogs/**",
            "/api/v1/groups/**",
            "/api/v1/userdiaries/**"
    };

    private final String[] USER_POST_API = {
            "/api/v1/reservation",
            "/api/v1/users/applicate-petsitter"
    };

    private final String[] PETSITTER_API = {
            "/api/v1/calendar/petsitter-calendar",
            "/api/v1/petsitter/**",
            "/api/v1/petsitter-diaries/**",
            "/api/v1/reservation/**"

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
                .antMatchers("/api/v1/petsitter/show-myinfo").hasRole("PETSITTER")
                .antMatchers("/ws/chat").permitAll()
                .antMatchers("/payment/test/*").permitAll()
                .antMatchers("/payment-cancel", "/payment-fail").permitAll()
                .antMatchers(HttpMethod.GET,GET_PERMIT_URL).permitAll()
                .antMatchers(POST_PERMIT_URL).permitAll()
                .antMatchers(DOC_URLS).permitAll()
                .antMatchers(HttpMethod.POST, USER_POST_API).hasRole("USER")
                .antMatchers(HttpMethod.GET, PETSITTER_APPLICANT_GET_API)
                .access("hasRole('PETSITTER') or hasRole('APPLICANT')")
                .antMatchers(HttpMethod.PUT, APPLICANT_PUT_API).hasRole("APPLICANT")
                .antMatchers(USER_API).hasRole("USER")
                .antMatchers(PETSITTER_API).hasRole("PETSITTER")
                .antMatchers(HttpMethod.GET, ADMIN_GET_API).hasRole("ADMIN")
                .antMatchers("/api/v1/admin/**", "/api/v1/category").hasRole("ADMIN")
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
