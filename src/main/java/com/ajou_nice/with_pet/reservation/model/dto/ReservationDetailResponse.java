package com.ajou_nice.with_pet.reservation.model.dto;

import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReservationDetailResponse {

    private Long reservationId;
    private Long dogId;
    private String dogName;
    private String dogImg;
    private Integer cost;
    private String checkIn;
    private String checkOut;
    private Double socializationTemperature;
    private Double affectionTemperature;
    private Integer socializationDegree;

    public static ReservationDetailResponse of(Reservation reservation) {
        return ReservationDetailResponse.builder()
                .reservationId(reservation.getReservationId())
                .dogId(reservation.getDog().getDogId())
                .dogName(reservation.getDog().getDogName())
                .dogImg(reservation.getDog().getDogProfileImg())
                .cost(reservation.getReservationTotalPrice())
                .checkIn(reservation.getReservationCheckIn().toLocalDate().toString())
                .checkOut(reservation.getReservationCheckOut().toLocalDate().toString())
                .socializationTemperature(reservation.getDog().getDogSocializationTemperature())
                .affectionTemperature(reservation.getDog().getDogAffectionTemperature())
                .socializationDegree(reservation.getDog().getDogSocializationDegree())
                .build();
    }
}
