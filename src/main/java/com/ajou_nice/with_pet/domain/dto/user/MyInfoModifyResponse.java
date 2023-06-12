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

    private Long userId;
    private String userName;

    public static MyInfoModifyResponse toMyInfoModifyResponse(User user) {
        return MyInfoModifyResponse.builder()
                .userId(user.getUserId())
                .userName(user.getName())
                .build();
    }
}
