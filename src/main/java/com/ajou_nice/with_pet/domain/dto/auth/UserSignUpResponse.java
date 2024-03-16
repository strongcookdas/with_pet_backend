package com.ajou_nice.with_pet.domain.dto.auth;

import com.ajou_nice.with_pet.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UserSignUpResponse {

    private Long userId;
    private String userName;

    public static UserSignUpResponse of(User user) {
        return UserSignUpResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .build();
    }

}
