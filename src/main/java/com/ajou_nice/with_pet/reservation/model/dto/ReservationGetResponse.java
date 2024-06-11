package com.ajou_nice.with_pet.reservation.model.dto;

import com.ajou_nice.with_pet.reservation.model.entity.ReservationPetSitterService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReservationGetResponse {
    private Integer reservationPrice;
    private String reservationServiceName;

    public static ReservationGetResponse of(ReservationPetSitterService reservationPetSitterService){
        return ReservationGetResponse.builder()
                .reservationPrice(reservationPetSitterService.getPrice())
                .reservationServiceName(reservationPetSitterService.getServiceName())
                .build();
    }

    public static List<ReservationGetResponse> toList(List<ReservationPetSitterService> reservationPetSitterServiceList){
        return reservationPetSitterServiceList.stream().map(reservationService -> ReservationGetResponse.builder()
                .reservationPrice(reservationService.getPrice())
                .reservationServiceName(reservationService.getServiceName())
                .build()).collect(Collectors.toList());
    }
}
