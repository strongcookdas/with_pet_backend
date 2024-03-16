package com.ajou_nice.with_pet.fixture;

import com.ajou_nice.with_pet.domain.dto.auth.UserSignInRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignInResponse;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpResponse;
import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import com.ajou_nice.with_pet.enums.UserRole;

public class UserDtoFixtures {
    public static UserSignUpRequest createUserSignUpRequest(String name, String email, String password, String passwordCheck,
                                                            String profileImg, String phone, AddressDto address) {
        return UserSignUpRequest.builder()
                .name(name)
                .email(email)
                .password(password)
                .passwordCheck(passwordCheck)
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

    public static UserSignInRequest createUserSignInRequest(String email, String password) {
        return UserSignInRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static UserSignInResponse createUserSignInResponse(String userName, String userProfile, UserRole userRole) {
        return UserSignInResponse.builder()
                .userProfile(userProfile)
                .userName(userName)
                .userRole(userRole.name())
                .build();
    }

}
