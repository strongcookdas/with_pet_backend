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
public class PetSitterReservationGetDetailResponse {

    private Long reservationId;
    private String reservationCheckIn;
    private String reservationCheckOut;
    private Integer reservationCost;
    private Long dogId;
    private String dogName;
    private String dogImg;
    private Double dogSocializationTemperature; // 펫시터가 평가하는 개 사회화 온도
    private Double dogAffectionTemperature; // 반려인의 일지 주기 체크
    private Integer dogSocializationDegree; // 반려인이 평가하는 개 사회화 정도

    public static PetSitterReservationGetDetailResponse of(Reservation reservation) {
        return PetSitterReservationGetDetailResponse.builder()
                .reservationId(reservation.getReservationId())
                .dogId(reservation.getDog().getDogId())
                .dogName(reservation.getDog().getDogName())
                .dogImg(reservation.getDog().getDogProfileImg())
                .reservationCost(reservation.getReservationTotalPrice())
                .reservationCheckIn(reservation.getReservationCheckIn().toLocalDate().toString())
                .reservationCheckOut(reservation.getReservationCheckOut().toLocalDate().toString())
                .dogSocializationTemperature(reservation.getDog().getDogSocializationTemperature())
                .dogAffectionTemperature(reservation.getDog().getDogAffectionTemperature())
                .dogSocializationDegree(reservation.getDog().getDogSocializationDegree())
                .build();
    }
}
