package com.ajou_nice.with_pet.user.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.controller.user.UserAuthController;
import com.ajou_nice.with_pet.domain.dto.auth.UserLoginRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserLoginResponse;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.service.user.UserAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserAuthController.class)
public class UserAuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAuthService userAuthService;

    @Autowired
    ObjectMapper objectMapper;

    UserLoginRequest userLoginRequest;
    UserLoginResponse userLoginResponse;

    @BeforeEach
    public void setup() {
        userLoginResponse = UserLoginResponse.builder().userName("some1")
                .userProfile("userProfile")
                .role(UserRole.ROLE_USER.name())
                .build();

        userLoginRequest = UserLoginRequest.builder().email("email@email.com")
                .password("password")
                .build();
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        when(userAuthService.login(any(), any()))
                .thenReturn(userLoginResponse);

        mockMvc.perform(post("/api/v2/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userLoginRequest))
                .andDo(print())
                .andExpect(status().isOk()));
    }
}
