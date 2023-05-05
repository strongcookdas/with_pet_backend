package com.ajou_nice.with_pet.domain.dto.user;

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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MyInfoModifyRequest {

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$", message = "올바른 이름을 입력하세요.")
    private String userName;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "영문, 특수문자, 숫자 포함 8자 이상의 패스워드를 입력하세요.")
    // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
    private String userPassword;

    @NotBlank
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String userEmail;
    private String profileImg;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 형식의 전화번호를 입력하세요.")
    private String phoneNum;
    private AddressDto address;
}
