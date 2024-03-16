package com.ajou_nice.with_pet.controller.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.user.AuthController;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignInRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignInResponse;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.UserDtoFixtures;
import com.ajou_nice.with_pet.service.user.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class SignInControllerTest extends CommonApiTest {

    final String USER_LOGIN_POST_API = "/api/v2/users/sign-in";
    UserSignInRequest userSignInRequest;
    UserSignInRequest invalidUserSignInRequestByEmail;
    UserSignInRequest invalidUserSignInRequestByPassword;
    UserSignInResponse userSignInResponse;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception {
        //given
        userSignInRequest = UserDtoFixtures.createUserSignInRequest("email@email.com", "password0302!");
        userSignInResponse = UserDtoFixtures.createUserSignInResponse("user",
                "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", UserRole.ROLE_USER);

        //when
        when(authService.SignIn(any()))
                .thenReturn(userSignInResponse);

        //then
        mockMvc.perform(post(USER_LOGIN_POST_API)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userSignInRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 : 이메일이 존재하지 않는 경우")
    @WithMockUser
    void login_fail_email_not_found() throws Exception {
        //given
        invalidUserSignInRequestByEmail = UserDtoFixtures.createUserSignInRequest("email2@email.com", "invalidPassword0302!");

        //when
        when(authService.SignIn(any()))
                .thenThrow(new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        //then
        mockMvc.perform(post(USER_LOGIN_POST_API)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invalidUserSignInRequestByEmail)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 : 패스워드가 다른 경우")
    @WithMockUser
    void login_fail_invalid_password() throws Exception {
        //given
        invalidUserSignInRequestByPassword = UserDtoFixtures.createUserSignInRequest("email@email.com", "invalidPassword0302!");

        //when
        when(authService.SignIn(any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage()));

        //then
        mockMvc.perform(post(USER_LOGIN_POST_API)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invalidUserSignInRequestByPassword)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
