package com.ajou_nice.with_pet.fixture.entity;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.UserRole;

public class UserFixture {
    public static User createUser(Long id, String name, String email, String password, UserRole userRole,
                                  String profileImg, String phone) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .role(userRole)
                .profileImg(profileImg)
                .phone(phone)
                .build();
    }
}
