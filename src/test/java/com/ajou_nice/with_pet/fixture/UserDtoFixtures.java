package com.ajou_nice.with_pet.fixture;

import com.ajou_nice.with_pet.domain.dto.auth.UserLoginRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserLoginResponse;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpResponse;
import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import com.ajou_nice.with_pet.enums.UserRole;

public class UserDtoFixtures {
    public static UserSignUpRequest createUserSignUpRequest(String name, String email, String password,
                                                            String profileImg, String phone, AddressDto address) {
        return UserSignUpRequest.builder()
                .name(name)
                .email(email)
                .password(password)
                .passwordCheck(password)
                .profileImg(profileImg)
                .phone(phone)
                .address(address)
                .build();
    }

    public static UserSignUpResponse createUserSignUpResponse(Long id, String name) {
        return UserSignUpResponse.builder()
                .userId(id)
                .userName(name)
                .build();
    }

    public static UserLoginRequest createUserLoginRequest(String email, String password) {
        return UserLoginRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static UserLoginResponse createUserLoginResponse(String userName, String userProfile, UserRole userRole) {
        return UserLoginResponse.builder()
                .userProfile(userProfile)
                .userName(userName)
                .role(userRole.name())
                .build();
    }
}
