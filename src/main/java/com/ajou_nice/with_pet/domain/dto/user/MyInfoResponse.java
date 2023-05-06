package com.ajou_nice.with_pet.domain.dto.user;

import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import com.ajou_nice.with_pet.domain.entity.User;
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
public class MyInfoResponse {

    private String userName;
    private String userId;
    private String profileImg;
    private String userEmail;
    private String phoneNum;
    private AddressDto address;

    public static MyInfoResponse toMyInfoResponse(User user) {
        return MyInfoResponse.builder()
                .userName(user.getName())
                .userId(user.getId())
                .profileImg(user.getProfileImg())
                .userEmail(user.getEmail())
                .phoneNum(user.getPhone())
                .address(AddressDto.of(user.getAddress()))
                .build();
    }
}
