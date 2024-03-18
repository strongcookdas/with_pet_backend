package com.ajou_nice.with_pet.fixture;

import com.ajou_nice.with_pet.domain.dto.auth.EmailRequest;

public class EmailDtoFixture {
    public static EmailRequest createEmailRequest(String email) {
        return EmailRequest.builder().email(email).build();
    }
}
