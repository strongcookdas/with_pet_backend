package com.ajou_nice.with_pet.reservation.model.dto;

import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.enums.PayStatus;
import com.ajou_nice.with_pet.reservation.model.entity.ReservationPetSitterService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class UserReservationGetInfosResponse {

    private List<UserReservationGetInfoResponse> reservationWaitStatusList;
    private List<UserReservationGetInfoResponse> reservationPayedStatusList;
    private List<UserReservationGetInfoResponse> reservationApproveStatusList;
    private List<UserReservationGetInfoResponse> reservationUseStatusList;
    private List<UserReservationGetInfoResponse> reservationDoneStatusList;

    public static UserReservationGetInfosResponse of(List<Reservation> waitReservations, List<Reservation> payedReservations,
                                                     List<Reservation> approveReservations,
                                                     List<Reservation> useReservations, List<Reservation> doneReservations) {
        return UserReservationGetInfosResponse.builder()
                .reservationWaitStatusList(UserReservationGetInfoResponse.toList(waitReservations, ""))
                .reservationPayedStatusList(
                        UserReservationGetInfoResponse.toList(payedReservations, PayStatus.SUCCESS.toString()))
                .reservationApproveStatusList(
                        UserReservationGetInfoResponse.toList(approveReservations, PayStatus.SUCCESS.toString()))
                .reservationUseStatusList(
                        UserReservationGetInfoResponse.toList(useReservations, PayStatus.SUCCESS.toString()))
                .reservationDoneStatusList(
                        UserReservationGetInfoResponse.toList(doneReservations, PayStatus.SUCCESS.toString()))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class UserReservationGetInfoResponse {

        private Long reservationId;
        private String reservationStatus;
        private String petSitterName;
        private String petSitterStreetAdr;
        private String dogName;
        private String dogImg;
        private LocalDateTime reservationCheckIn;
        private LocalDateTime reservationCheckOut;
        private int reservationCost;
        private String reservationPayStatus;

        private String reservationCriticalServiceName;
        private int reservationCriticalServicePrice;

        private List<ReservationGetServiceResponse> reservationServiceResponses;

        public static List<UserReservationGetInfoResponse> toList(List<Reservation> reservations, String payStatus) {
            return reservations.stream().map(reservation -> UserReservationGetInfoResponse.builder()
                    .reservationId(reservation.getReservationId())
                    .reservationStatus(reservation.getReservationStatus().toString())
                    .petSitterName(reservation.getPetSitter().getPetSitterName())
                    .dogName(reservation.getDog().getDogName())
                    .dogImg(reservation.getDog().getDogProfileImg())
                    .reservationCheckIn(reservation.getReservationCheckIn())
                    .reservationCheckOut(reservation.getReservationCheckOut())
                    .reservationCost(reservation.getReservationTotalPrice())
                    .petSitterStreetAdr(reservation.getPetSitter().getPetSitterStreetAdr())
                    .reservationPayStatus(payStatus)
                    .reservationCriticalServiceName(reservation.getCriticalServiceName())
                    .reservationCriticalServicePrice(reservation.getCriticalServicePrice())
                    .reservationServiceResponses(reservation.getReservationPetSitterServiceList() == null ?
                            null
                            : ReservationGetServiceResponse.toList(reservation.getReservationPetSitterServiceList()))
                    .build()).collect(Collectors.toList());
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ReservationGetServiceResponse {

        private Integer price;
        private String serviceName;

        public static ReservationGetServiceResponse of(
                ReservationPetSitterService reservationPetSitterService) {
            return ReservationGetServiceResponse.builder()
                    .price(reservationPetSitterService.getPrice())
                    .serviceName(reservationPetSitterService.getServiceName())
                    .build();
        }

        public static List<ReservationGetServiceResponse> toList(
                List<ReservationPetSitterService> reservationPetSitterServiceList) {
            return reservationPetSitterServiceList.stream()
                    .map(reservationService -> ReservationGetServiceResponse.builder()
                            .price(reservationService.getPrice())
                            .serviceName(reservationService.getServiceName())
                            .build()).collect(Collectors.toList());
        }
    }

}
