package com.ajou_nice.with_pet.reservation.model.dto;

import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReservationResponse {
    //월별 펫시터 예약 조회용 Response
    private Long reservationId;
    private Long dogId;
    private String dogName;
    private String checkIn;
    private String checkOut;
    private ReservationStatus reservationStatus;

    public static ReservationResponse of(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .dogId(reservation.getDog().getDogId())
                .dogName(reservation.getDog().getDogName())
                .checkIn(reservation.getReservationCheckIn().toLocalDate().toString())
                .checkOut(reservation.getReservationCheckOut().toLocalDate().toString())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
