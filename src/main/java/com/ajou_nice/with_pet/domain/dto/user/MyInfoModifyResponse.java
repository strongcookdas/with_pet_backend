package com.ajou_nice.with_pet.domain.dto.user;

import com.ajou_nice.with_pet.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class MyInfoModifyResponse {

    private String userName;
    private String userProfile;
    private String role;

    public static MyInfoModifyResponse toMyInfoModifyResponse(User user) {
        return MyInfoModifyResponse.builder()
                .userName(user.getName())
                .userProfile(user.getProfileImg())
                .role(user.getRole().name())
                .build();
    }
}
