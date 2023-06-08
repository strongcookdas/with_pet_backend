package com.ajou_nice.with_pet.domain.dto.reservation;

import com.ajou_nice.with_pet.domain.dto.reservation.ReservationCreateResponse.ReservationServiceResponse;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
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
    private Long dogId;
    private String dogName;
    private String dogGender;
    private String dogProfileImg;
    private String dogBreed;
    private String dogIsbn;
    private Boolean dogNeutralization;
    private Double dogSocializationTemperature;
    private Integer dogSocializationDegree;
    private Double dogAffectionTemperature;
    private Integer totalCost;

    private String criticalServiceName;
    private Integer criticalServicePrice;
    private List<ReservationServiceResponse> reservationServiceResponses;

    private String checkIn;
    private String checkOut;
    private ReservationStatus reservationStatus;

    public static ReservationResponse of(Reservation reservation) {
        return ReservationResponse.builder()
                .dogId(reservation.getDog().getDogId())
                .dogName(reservation.getDog().getName())
                .dogGender(reservation.getDog().getGender())
                .dogProfileImg(reservation.getDog().getProfile_img())
                .dogBreed(reservation.getDog().getBreed())
                .dogIsbn(reservation.getDog().getIsbn())
                .dogNeutralization(reservation.getDog().getNeutralization())
                .dogSocializationTemperature(reservation.getDog().getSocializationTemperature())
                .dogSocializationDegree(reservation.getDog().getSocializationDegree())
                .dogAffectionTemperature(reservation.getDog().getAffectionTemperature())
                .totalCost(reservation.getTotalPrice())
                .criticalServiceName(reservation.getCriticalServiceName())
                .criticalServicePrice(reservation.getCriticalServicePrice())
                .reservationServiceResponses(reservation.getReservationPetSitterServiceList() == null ?
                        null : ReservationServiceResponse.toList(reservation.getReservationPetSitterServiceList()))
                .checkIn(reservation.getCheckIn().toLocalDate().toString())
                .checkOut(reservation.getCheckOut().toLocalDate().toString())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
