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
public class PetSitterReservationPatchApprovalResponse {

    private Long reservationId;
    private Integer reservationTotalCost;
    private String reservationCheckIn;
    private String reservationCheckOut;
    private Long dogId;
    private String dogName;
    private String dogImg;
    private Double dogSocializationTemperature;
    private Double dogAffectionTemperature;
    private Integer dogSocializationDegree;

    public static PetSitterReservationPatchApprovalResponse of(Reservation reservation) {
        return PetSitterReservationPatchApprovalResponse.builder()
                .reservationId(reservation.getReservationId())
                .dogId(reservation.getDog().getDogId())
                .dogName(reservation.getDog().getDogName())
                .dogImg(reservation.getDog().getDogProfileImg())
                .reservationTotalCost(reservation.getReservationTotalPrice())
                .reservationCheckIn(reservation.getReservationCheckIn().toLocalDate().toString())
                .reservationCheckOut(reservation.getReservationCheckOut().toLocalDate().toString())
                .dogSocializationTemperature(reservation.getDog().getDogSocializationTemperature())
                .dogAffectionTemperature(reservation.getDog().getDogAffectionTemperature())
                .dogSocializationDegree(reservation.getDog().getDogSocializationDegree())
                .build();
    }
}
