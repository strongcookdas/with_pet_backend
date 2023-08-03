package com.ajou_nice.with_pet.user.auth.signup;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpResponse;
import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
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

public class SignUpServiceTest {
    UserAuthService userAuthService;
    UserRepository userRepository = mock(UserRepository.class);
    BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    ValidateCollection validateCollection = mock(ValidateCollection.class);

    UserSignUpRequest userSignUpRequest;
    UserSignUpResponse userSignUpResponse;
    AddressDto address;
    User user;

    @BeforeEach
    public void setUp(){
        address = AddressDto.builder()
                .zipcode("111111")
                .streetAdr("월드컵로")
                .detailAdr("팔달관")
                .build();

        userSignUpResponse = UserSignUpResponse.builder()
                .userId(1L)
                .userName("user1")
                .build();

        user = User.builder()
                .userId(1L)
                .name("user1")
                .email("email@email.com")
                .password("password")
                .role(UserRole.ROLE_USER)
                .profileImg("image")
                .phone("010-0000-0000")
                .build();
        userAuthService = new UserAuthService(userRepository, encoder, jwtTokenUtil, validateCollection);
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        //given
        userSignUpRequest = UserSignUpRequest.builder()
                .name("user1")
                .email("email@email.com")
                .password("password")
                .passwordCheck("password")
                .profileImg("image")
                .phone("010-0000-0000")
                .address(address)
                .build();
        //when
        when(userRepository.existsByEmail(userSignUpRequest.getEmail()))
                .thenReturn(false);
        when(userRepository.save(any()))
                .thenReturn(user);
        //then
        UserSignUpResponse result = userAuthService.signUp(userSignUpRequest);
        assertEquals(result.getUserId(),user.getUserId());
        assertEquals(result.getUserName(),user.getName());
    }

    @Test
    @DisplayName("회원가입 실패1 : 아이디 중복")
    void signUp_fail1() {
        //given
        userSignUpRequest = UserSignUpRequest.builder()
                .name("user1")
                .email("email@email.com")
                .password("password")
                .passwordCheck("password")
                .profileImg("image")
                .phone("010-0000-0000")
                .address(address)
                .build();
        //when
        when(userRepository.existsByEmail(userSignUpRequest.getEmail()))
                .thenReturn(true);
        //then
        AppException exception = assertThrows(AppException.class,() -> userAuthService.signUp(userSignUpRequest));
        assertEquals(ErrorCode.DUPLICATED_EMAIL,exception.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 실패2 : 패스워드 불일치")
    void signUp_fail2() {
        //given
        userSignUpRequest = UserSignUpRequest.builder()
                .name("user1")
                .email("email@email.com")
                .password("password1")
                .passwordCheck("password2")
                .profileImg("image")
                .phone("010-0000-0000")
                .address(address)
                .build();
        //when
        //then
        AppException exception = assertThrows(AppException.class,() -> userAuthService.signUp(userSignUpRequest));
        assertEquals(ErrorCode.PASSWORD_COMPARE_FAIL,exception.getErrorCode());
    }
}
