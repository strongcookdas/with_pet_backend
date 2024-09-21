package com.ajou_nice.with_pet.pay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RefundResponse {

    private String message;

    public static RefundResponse of(String message) {
        return RefundResponse.builder()
                .message(message)
                .build();
    }
}
