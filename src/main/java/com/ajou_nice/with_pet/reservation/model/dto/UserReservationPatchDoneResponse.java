package com.ajou_nice.with_pet.reservation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserReservationPatchDoneResponse {
    private String message;

    public static UserReservationPatchDoneResponse of(String message) {
        return UserReservationPatchDoneResponse.builder()
                .message(message)
                .build();
    }
}
