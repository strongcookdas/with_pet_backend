package com.ajou_nice.with_pet.reservation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PetSitterReservationPutEvaluateDogResponse {

    private String message;

    public static PetSitterReservationPutEvaluateDogResponse of(String message) {
        return PetSitterReservationPutEvaluateDogResponse.builder()
                .message(message)
                .build();
    }
}
