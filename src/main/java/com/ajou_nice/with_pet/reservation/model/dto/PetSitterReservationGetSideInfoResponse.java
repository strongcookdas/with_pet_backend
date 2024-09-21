package com.ajou_nice.with_pet.reservation.model.dto;

import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PetSitterReservationGetSideInfoResponse {

    private List<PetSitterReservationGetDetailResponse> useReservations;
    private List<PetSitterReservationGetDetailResponse> newReservations;
    private List<PetSitterReservationGetDetailResponse> doneReservations;
    private Integer reservationMonthProfit;

    public static PetSitterReservationGetSideInfoResponse of(List<Reservation> use, List<Reservation> wait,
                                                             List<Reservation> done, Integer monthProfit) {
        return PetSitterReservationGetSideInfoResponse.builder()
                .useReservations(use.stream().map(PetSitterReservationGetDetailResponse::of)
                        .collect(Collectors.toList()))
                .newReservations(wait.stream().map(PetSitterReservationGetDetailResponse::of)
                        .collect(Collectors.toList()))
                .doneReservations(done.stream().map(PetSitterReservationGetDetailResponse::of).collect(
                        Collectors.toList()))
                .reservationMonthProfit(monthProfit)
                .build();
    }
}
