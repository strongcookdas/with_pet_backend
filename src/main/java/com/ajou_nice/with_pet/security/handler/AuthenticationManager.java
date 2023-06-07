package com.ajou_nice.with_pet.security.handler;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 유효하지 않은 토큰에 대한 예외 핸들러
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Response errorResponse = Response.error(ErrorCode.INVALID_TOKEN.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.sendRedirect("http://ec2-13-125-242-183.ap-northeast-2.compute.amazonaws.com/login");
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}
