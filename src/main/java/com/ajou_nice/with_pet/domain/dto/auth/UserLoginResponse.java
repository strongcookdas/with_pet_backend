package com.ajou_nice.with_pet.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginResponse {

    private String result;

    public static UserLoginResponse of(String result) {
        return UserLoginResponse.builder()
                .result(result)
                .build();
    }
}
