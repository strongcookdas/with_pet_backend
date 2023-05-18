package com.ajou_nice.with_pet.domain.dto.reservation;

import com.ajou_nice.with_pet.domain.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReservationDetailResponse {

    private Long dogId;
    private String dogName;
    private Integer cost;
    private String checkIn;
    private String checkOut;
    private Double socializationTemperature;
    private Double affectionTemperature;
    private Integer socializationDegree;

    public static ReservationDetailResponse of(Reservation reservation) {
        return ReservationDetailResponse.builder()
                .dogId(reservation.getDog().getDogId())
                .dogName(reservation.getDog().getName())
                .cost(reservation.getPay().getCost())
                .checkIn(reservation.getCheckIn().toLocalDate().toString())
                .checkOut(reservation.getCheckOut().toLocalDate().toString())
                .socializationTemperature(reservation.getDog().getSocializationTemperature())
                .affectionTemperature(reservation.getDog().getAffectionTemperature())
                .socializationDegree(reservation.getDog().getSocializationDegree())
                .build();
    }
}
