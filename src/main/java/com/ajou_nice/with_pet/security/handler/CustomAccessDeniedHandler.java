package com.ajou_nice.with_pet.security.handler;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 접근 권한이 없는 예외 핸들러
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Response errorResponse = Response.error(ErrorCode.INVALID_PERMISSION.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
