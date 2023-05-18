package com.ajou_nice.with_pet.domain.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReservationStatusRequest {

    private Long reservationId;
    private String status;
}
