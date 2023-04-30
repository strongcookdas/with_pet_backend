package com.ajou_nice.with_pet.domain.dto.user;

import com.ajou_nice.with_pet.domain.embedded.AddressDto;
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
@Setter
@Builder
@ToString
public class UserSignUpRequest {

    private String userName;
    private String userId;
    private String userPassword;
    private String userPasswordCheck;
    private String userEmail;
    private String profileImg;
    private String phoneNum;
    private AddressDto address;

}
