package com.ajou_nice.with_pet.controller.auth;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.auth.controller.AuthController;
import com.ajou_nice.with_pet.domain.dto.auth.EmailRequest;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.EmailDtoFixture;
import com.ajou_nice.with_pet.auth.service.AuthService;
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

import javax.validation.Validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class CheckDuplicatedEmailControllerTest extends CommonApiTest {
    final String AUTH_CHECK_DUPLICATED_EMAIL_URI = "/api/v2/users/email-duplicates";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthService authService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Validator validator;

    EmailRequest emailRequest;

    @Test
    @DisplayName("이메일 중복 확인 성공")
    @WithMockUser
    void checkDuplicatedEmail_success() throws Exception {
        emailRequest = EmailDtoFixture.createEmailRequest("email@email.com");
        String RESPONSE_MESSAGE = "사용할 수 있는 이메일입니다.";
        when(authService.checkDuplicatedEmail(emailRequest))
                .thenReturn(RESPONSE_MESSAGE);
        mockMvc.perform(post(AUTH_CHECK_DUPLICATED_EMAIL_URI)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(emailRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 중복 확인 실패 : 중복된 이메일이 있는 경우")
    @WithMockUser
    void checkDuplicatedEmail_fail_duplicated_email() throws Exception {
        emailRequest = EmailDtoFixture.createEmailRequest("email2@email.com");
        when(authService.checkDuplicatedEmail(any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_EMAIL, ErrorCode.DUPLICATED_EMAIL.getMessage()));
        mockMvc.perform(post(AUTH_CHECK_DUPLICATED_EMAIL_URI)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(emailRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
