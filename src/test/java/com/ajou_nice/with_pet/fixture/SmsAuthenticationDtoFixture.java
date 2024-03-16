package com.ajou_nice.with_pet.fixture;

import com.ajou_nice.with_pet.domain.dto.sms.SmsAuthenticationRequest;

public class SmsAuthenticationDtoFixture {
    public static SmsAuthenticationRequest createSmsAuthenticationRequest(String phone, String authenticationNumber) {
        return SmsAuthenticationRequest.builder()
                .phone(phone)
                .authenticationNumber(authenticationNumber)
                .build();
    }
}
