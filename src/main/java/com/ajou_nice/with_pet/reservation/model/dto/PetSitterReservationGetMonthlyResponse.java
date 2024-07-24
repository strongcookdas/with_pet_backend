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
public class PetSitterReservationGetMonthlyResponse {
    //월별 펫시터 예약 조회용 Response
    private Long reservationId;
    private Long dogId;
    private String dogName;
    private String reservationCheckIn;
    private String reservationCheckOut;
    private ReservationStatus reservationStatus;

    public static PetSitterReservationGetMonthlyResponse of(Reservation reservation) {
        return PetSitterReservationGetMonthlyResponse.builder()
                .reservationId(reservation.getReservationId())
                .dogId(reservation.getDog().getDogId())
                .dogName(reservation.getDog().getDogName())
                .reservationCheckIn(reservation.getReservationCheckIn().toLocalDate().toString())
                .reservationCheckOut(reservation.getReservationCheckOut().toLocalDate().toString())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
