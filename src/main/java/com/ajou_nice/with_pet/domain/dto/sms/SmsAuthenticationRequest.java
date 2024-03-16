package com.ajou_nice.with_pet.domain.dto.sms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SmsAuthenticationRequest {
    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 형식의 전화번호를 입력하세요.")
    private String phone;
    @NotBlank(message = "인증번호를 입력하세요.")
    private String authenticationNumber;
}
