package com.ajou_nice.with_pet.domain.dto.calendar;

import com.ajou_nice.with_pet.reservation.model.dto.ReservationDetailResponse;
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
public class PetSitterSideBarResponse {

    private List<ReservationDetailResponse> useReservations;
    private List<ReservationDetailResponse> newReservations;
    private List<ReservationDetailResponse> doneReservations;
    private Integer monthProfit;

    public static PetSitterSideBarResponse of(List<Reservation> use, List<Reservation> wait,
            List<Reservation> done, Integer monthProfit) {
        return PetSitterSideBarResponse.builder()
                .useReservations(use.stream().map(ReservationDetailResponse::of)
                        .collect(Collectors.toList()))
                .newReservations(wait.stream().map(ReservationDetailResponse::of)
                        .collect(Collectors.toList()))
                .doneReservations(done.stream().map(ReservationDetailResponse::of).collect(
                        Collectors.toList()))
                .monthProfit(monthProfit)
                .build();
    }
}
