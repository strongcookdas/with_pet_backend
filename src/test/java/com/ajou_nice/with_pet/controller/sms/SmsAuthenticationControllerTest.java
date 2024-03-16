package com.ajou_nice.with_pet.controller.sms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.SmsAuthenticationController;
import com.ajou_nice.with_pet.domain.dto.sms.SmsAuthenticationRequest;
import com.ajou_nice.with_pet.fixture.SmsAuthenticationDtoFixture;
import com.ajou_nice.with_pet.service.SmsAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SmsAuthenticationController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class SmsAuthenticationControllerTest extends CommonApiTest {
    private final String SMS_AUTHENTICATION_URI = "/api/v2/sms-authentication";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    SmsAuthenticationService smsAuthenticationService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Validator validator;

    SmsAuthenticationRequest authenticationRequest;

    @Test
    @DisplayName("인증번호 발송 성공")
    @WithMockUser
    void sendOne_success() throws Exception {
        String RESPONSE_MESSAGE = "인증번호가 발송되었습니다.";
        String to = "010-0000-0000";
        when(smsAuthenticationService.sendOne(any(), any(), any()))
                .thenReturn(RESPONSE_MESSAGE);
        mockMvc.perform(get(SMS_AUTHENTICATION_URI)
                        .with(csrf())
                        .param("to",to)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(RESPONSE_MESSAGE)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증번호 발송 실패 : 발신번호가 없을 경우")
    @WithMockUser
    void sendOne_fail_without_phone() throws Exception {
        String RESPONSE_MESSAGE = "인증번호가 발송되었습니다.";
        when(smsAuthenticationService.sendOne(any(), any(), any()))
                .thenReturn(RESPONSE_MESSAGE);
        mockMvc.perform(get(SMS_AUTHENTICATION_URI)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(RESPONSE_MESSAGE)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증번호 비교 성공")
    @WithMockUser
    void compareAuthenticationNumber_success() throws Exception {
        //given
        authenticationRequest = SmsAuthenticationDtoFixture.createSmsAuthenticationRequest("010-0000-0000","1234");
        String RESPONSE_MESSAGE = "인증되었습니다.";

        when(smsAuthenticationService.compareAuthenticationNumber(authenticationRequest))
                .thenReturn(RESPONSE_MESSAGE);
        mockMvc.perform(post(SMS_AUTHENTICATION_URI)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(authenticationRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 유효성 검사")
    void validSignUp() {
        //given
        authenticationRequest = SmsAuthenticationDtoFixture.createSmsAuthenticationRequest("010-","");

        //when
        Set<ConstraintViolation<SmsAuthenticationRequest>> validate = validator.validate(authenticationRequest);

        //then
        Iterator<ConstraintViolation<SmsAuthenticationRequest>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<SmsAuthenticationRequest> next = iterator.next();
            messages.add(next.getMessage());
        }

        Assertions.assertThat(messages)
                .contains("올바른 형식의 전화번호를 입력하세요.","인증번호를 입력하세요.");
    }
}
