package com.ajou_nice.with_pet.domain.dto.auth;

import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "올바른 이름을 입력하세요.")
    private String userName;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$", message = "시작은 영문으로만, '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하로 이루어진 아이디를 입력하세요.")
    //시작은 영문으로만, '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하
    private String userId;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "영문, 특수문자, 숫자 포함 8자 이상의 패스워드를 입력하세요.")
    // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
    private String userPassword;
    @NotBlank(message = "패스워드 확인란에 패스워드를 입력하세요.")
    private String userPasswordCheck;
    @NotBlank
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String userEmail;
    private String profileImg;
    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 형식의 전화번호를 입력하세요.")
    private String phoneNum;
    private AddressDto address;
}
