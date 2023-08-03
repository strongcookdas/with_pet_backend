package com.ajou_nice.with_pet.user.auth.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ajou_nice.with_pet.domain.dto.auth.UserLoginRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserLoginResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.service.user.UserAuthService;
import com.ajou_nice.with_pet.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserAuthLoginServiceTest {

    UserAuthService userAuthService;
    UserRepository userRepository = mock(UserRepository.class);
    BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    ValidateCollection validateCollection = mock(ValidateCollection.class);

    UserLoginRequest userLoginRequest;
    UserLoginResponse userLoginResponse;
    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .name("user1")
                .email("email@email.com")
                .password("password")
                .role(UserRole.ROLE_USER)
                .profileImg("userProfile")
                .build();
        userAuthService = new UserAuthService(userRepository, encoder, jwtTokenUtil, validateCollection);
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        //given
        userLoginRequest = UserLoginRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();
        //when
        when(validateCollection.userValidation(userLoginRequest.getEmail()))
                .thenReturn(user);
        when(encoder.matches(userLoginRequest.getPassword(), user.getPassword())).
                thenReturn(true);
        //then
        userLoginResponse = userAuthService.login(userLoginRequest);
        assertEquals(userLoginResponse.getUserName(),user.getName());
        assertEquals(userLoginResponse.getUserProfile(),user.getProfileImg());
        assertEquals(userLoginResponse.getRole(),user.getRole().name());
    }

    @Test
    @DisplayName("로그인 실패1 : 유저가 존재하지 않은 경우")
    void login_fail1() {
        //given
        userLoginRequest = UserLoginRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();
        //when
        when(validateCollection.userValidation(userLoginRequest.getEmail()))
                .thenThrow(new AppException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));
        //then
        AppException exception = assertThrows(AppException.class,() -> userAuthService.login(userLoginRequest));
        assertEquals(ErrorCode.USER_NOT_FOUND,exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패2 : 패스워드가 같지 않은 경우")
    void login_fail2() {
        //given
        userLoginRequest = UserLoginRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();
        //when
        when(validateCollection.userValidation(userLoginRequest.getEmail()))
                .thenReturn(user);
        when(encoder.matches(userLoginRequest.getPassword(), user.getPassword())).
                thenReturn(false);
        //then
        AppException exception = assertThrows(AppException.class,() -> userAuthService.login(userLoginRequest));
        assertEquals(ErrorCode.INVALID_PASSWORD,exception.getErrorCode());
    }
}
