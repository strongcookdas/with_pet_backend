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

    private Long dogId;
    private String dogName;
    private String checkIn;
    private String checkOut;
    private ReservationStatus reservationStatus;

    public static ReservationResponse of(Reservation reservation) {
        return ReservationResponse.builder()
                .dogId(reservation.getDog().getDogId())
                .dogName(reservation.getDog().getName())
                .checkIn(reservation.getCheckIn().toLocalDate().toString())
                .checkOut(reservation.getCheckOut().toLocalDate().toString())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
