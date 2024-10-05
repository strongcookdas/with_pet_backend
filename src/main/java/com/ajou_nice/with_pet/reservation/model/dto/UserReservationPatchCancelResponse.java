package com.ajou_nice.with_pet.reservation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserReservationPatchCancelResponse {
    private String message;

    public static UserReservationPatchCancelResponse of(String message) {
        return UserReservationPatchCancelResponse.builder()
                .message(message)
                .build();
    }
}
