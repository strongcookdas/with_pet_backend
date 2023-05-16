package com.ajou_nice.with_pet.domain.dto.reservation;

import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReservationResponse {

    private Long reservationId;
    private Long userId;
    private Long dogId;
    private Long petsitterId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private ReservationStatus reservationStatus;

    public static ReservationResponse of(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .userId(reservation.getUser().getUserId())
                .dogId(reservation.getDog().getDogId())
                .petsitterId(reservation.getPetSitter().getId())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
