package com.ajou_nice.with_pet.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ajou_nice.with_pet.domain.dto.auth.UserSignInRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignInResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.UserDtoFixtures;
import com.ajou_nice.with_pet.fixture.entity.UserFixture;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.auth.service.AuthService;
import com.ajou_nice.with_pet.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SignInServiceTest {

    AuthService authService;
    UserRepository userRepository = mock(UserRepository.class);
    BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    ValidateCollection validateCollection = mock(ValidateCollection.class);

    UserSignInRequest userSignInRequest;
    UserSignInResponse userSignInResponse;
    User user;

    @BeforeEach
    void setUp() {
        user = UserFixture.createUser(1L, "user", "email@email.com", "password0302!", UserRole.ROLE_USER,
                "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000");
        authService = new AuthService(userRepository, encoder, jwtTokenUtil, validateCollection);
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        //given
        userSignInRequest = UserDtoFixtures.createUserSignInRequest("email@email.com", "password0302!");
        //when
        when(validateCollection.userValidationByEmail(userSignInRequest.getEmail()))
                .thenReturn(user);
        when(encoder.matches(userSignInRequest.getPassword(), user.getPassword())).
                thenReturn(true);
        //then
        userSignInResponse = authService.SignIn(userSignInRequest);
        assertEquals(userSignInResponse.getUserName(), user.getName());
        assertEquals(userSignInResponse.getUserProfile(), user.getProfileImg());
        assertEquals(userSignInResponse.getUserRole(), user.getRole().name());
    }

    @Test
    @DisplayName("로그인 실패 : 이메일이 존재하지 않은 경우")
    void login_fail_email_not_found() {
        //given
        userSignInRequest = UserDtoFixtures.createUserSignInRequest("invalid@email.con", "password0302!");
        //when
        when(validateCollection.userValidationByEmail(userSignInRequest.getEmail()))
                .thenThrow(new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //then
        AppException exception = assertThrows(AppException.class, () -> authService.SignIn(userSignInRequest));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패 : 패스워드가 다른 경우")
    void login_fail_invalid_password() {
        //given
        userSignInRequest = UserDtoFixtures.createUserSignInRequest("email@email.com", "password0203!");
        //when
        when(validateCollection.userValidationByEmail(userSignInRequest.getEmail()))
                .thenReturn(user);
        when(encoder.matches(userSignInRequest.getPassword(), user.getPassword())).
                thenReturn(false);
        //then
        AppException exception = assertThrows(AppException.class, () -> authService.SignIn(userSignInRequest));
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}
