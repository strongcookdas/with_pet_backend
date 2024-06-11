package com.ajou_nice.with_pet.service.auth;

import com.ajou_nice.with_pet.domain.dto.auth.EmailRequest;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.EmailDtoFixture;
import com.ajou_nice.with_pet.fixture.entity.UserFixture;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.auth.service.AuthService;
import com.ajou_nice.with_pet.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckDuplicatedEmailServiceTest {
    AuthService authService;
    UserRepository userRepository = mock(UserRepository.class);
    BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    ValidateCollection validateCollection = mock(ValidateCollection.class);
    User user;
    EmailRequest emailRequest;

    @BeforeEach
    void setUp() {
        user = UserFixture.createUser(1L, "user", "email@email.com", "password0302!", UserRole.ROLE_USER,
                "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000");
        authService = new AuthService(userRepository, encoder, jwtTokenUtil, validateCollection);
    }

    @Test
    @DisplayName("이메일 중복 확인 성공")
    @WithMockUser
    void checkDuplicatedEmail_success() {
        //given
        emailRequest = EmailDtoFixture.createEmailRequest("email@email.com");
        String RESPONSE_MESSAGE = "사용할 수 있는 이메일입니다.";
        //when
        when(userRepository.existsByEmail(emailRequest.getEmail()))
                .thenReturn(false);
        //then
        String response = authService.checkDuplicatedEmail(emailRequest);
        assertEquals(response, RESPONSE_MESSAGE);
    }

    @Test
    @DisplayName("이메일 중복 확인 실패 : 중복된 이메일이 있는 경우")
    @WithMockUser
    void checkDuplicatedEmail_fail_duplicated_email() {
        //given
        emailRequest = EmailDtoFixture.createEmailRequest("email2@email.com");
        //when
        when(userRepository.existsByEmail(emailRequest.getEmail()))
                .thenReturn(true);
        //then
        AppException exception = assertThrows(AppException.class, () -> authService.checkDuplicatedEmail(emailRequest));
        assertEquals(ErrorCode.DUPLICATED_EMAIL, exception.getErrorCode());
    }
}
