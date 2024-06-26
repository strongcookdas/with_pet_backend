package com.ajou_nice.with_pet.service.auth;


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
import com.ajou_nice.with_pet.fixture.AddressDtoFixture;
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

public class SignUpServiceTest {
    AuthService authService;
    UserRepository userRepository = mock(UserRepository.class);
    BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    ValidateCollection validateCollection = mock(ValidateCollection.class);

    UserSignUpRequest userSignUpRequest;
    UserSignUpResponse userSignUpResponse;
    AddressDto address;
    User user;

    @BeforeEach
    public void setUp() {
        address = AddressDtoFixture.createAddressDto("16499", "경기도 수원시 영통구 월드컵로 206 (원천동, 아주대학교)", "팔달관");
        userSignUpResponse = UserDtoFixtures.createUserSignUpResponse(1L, "user");
        user = UserFixture.createUser(1L, "user", "email@email.com", "password0302!", UserRole.ROLE_USER,
                "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000");
        authService = new AuthService(userRepository, encoder, jwtTokenUtil, validateCollection);
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        //given
        userSignUpRequest = UserDtoFixtures.createUserSignUpRequest("user","email@email.com","password0302!", "password0302!", "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png","010-0000-0000", address);
        //when
        when(userRepository.existsByEmail(userSignUpRequest.getEmail()))
                .thenReturn(false);
        when(userRepository.save(any()))
                .thenReturn(user);
        //then
        UserSignUpResponse result = authService.signUp(userSignUpRequest);
        assertEquals(result.getUserId(), user.getId());
        assertEquals(result.getUserName(), user.getName());
    }

    @Test
    @DisplayName("회원가입 실패 : email 중복")
    void signUp_fail_duplicated_email() {
        //given
        userSignUpRequest = UserDtoFixtures.createUserSignUpRequest("user","email@email.com","password0302!","password0302!","https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png","010-0000-0000", address);
        //when
        when(userRepository.existsByEmail(userSignUpRequest.getEmail()))
                .thenReturn(true);
        //then
        AppException exception = assertThrows(AppException.class, () -> authService.signUp(userSignUpRequest));
        assertEquals(ErrorCode.DUPLICATED_EMAIL, exception.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 실패 : 패스워드 불일치")
    void signUp_fail_invalid_password() {
        //given
        userSignUpRequest = UserDtoFixtures.createUserSignUpRequest("user","email@email.com","password0302!","password0302","https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png","010-0000-0000", address);
        //when
        when(userRepository.existsByEmail(userSignUpRequest.getEmail()))
                .thenReturn(false);
        //then
        AppException exception = assertThrows(AppException.class, () -> authService.signUp(userSignUpRequest));
        assertEquals(ErrorCode.PASSWORD_COMPARE_FAIL, exception.getErrorCode());
    }
}
