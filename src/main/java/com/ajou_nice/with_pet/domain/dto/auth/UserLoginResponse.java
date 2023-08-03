package com.ajou_nice.with_pet.domain.dto.auth;

import com.ajou_nice.with_pet.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserLoginResponse {

    private String userName;
    private String userProfile;
    private String role;
    @JsonIgnore
    private String token;

    public static UserLoginResponse of(User user, String token) {
        return UserLoginResponse.builder()
                .userName(user.getName())
                .userProfile(user.getProfileImg())
                .role(user.getRole().name())
                .token(token)
                .build();
    }
}
