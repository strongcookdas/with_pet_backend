package com.ajou_nice.with_pet.domain.dto.auth;

import com.ajou_nice.with_pet.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UserSignInResponse {

    private String userName;
    private String userProfile;
    private String userRole;
    @JsonIgnore
    private String token;

    public static UserSignInResponse of(User user, String token) {
        return UserSignInResponse.builder()
                .userName(user.getName())
                .userProfile(user.getProfileImg())
                .userRole(user.getRole().name())
                .token(token)
                .build();
    }
}
