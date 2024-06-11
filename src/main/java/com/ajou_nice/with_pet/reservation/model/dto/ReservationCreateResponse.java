package com.ajou_nice.with_pet.reservation.model.dto;

import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.reservation.model.entity.ReservationPetSitterService;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
// 예약 생성 후 줄 response
public class ReservationCreateResponse {

	private Long reservationId;

	private String reservationDate;
	private String reservationCheckIn;
	private String reservationCheckOut;
	private String dogName;
	private String dogSize;
	private String petSitterName;

	private String reservationCriticalServiceName;
	private Integer reservationCriticalServicePrice;

	private List<ReservationGetResponse> reservationServiceResponses;
	private Integer reservationCost;

	public static ReservationCreateResponse of(Reservation reservation){
		return ReservationCreateResponse.builder()
				.reservationId(reservation.getReservationId())
				.reservationDate(reservation.getCreatedAt().toString())
				.reservationCheckIn(reservation.getReservationCheckIn().toString())
				.reservationCheckOut(reservation.getReservationCheckOut().toString())
				.dogName(reservation.getDog().getDogName())
				.dogSize(reservation.getDog().getDogSize().toString())
				.petSitterName(reservation.getPetSitter().getPetSitterName())
				.reservationCriticalServiceName(reservation.getCriticalServiceName())
				.reservationCriticalServicePrice(reservation.getCriticalServicePrice())
				.reservationServiceResponses(reservation.getReservationPetSitterServiceList() == null ?
						null : ReservationGetResponse.toList(reservation.getReservationPetSitterServiceList()))
				.reservationCost(reservation.getReservationTotalPrice())
				.build();


	}
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Getter
	public static class ReservationServiceResponse{

		private Integer price;
		private String serviceName;

		public static ReservationServiceResponse of(ReservationPetSitterService reservationPetSitterService){
			return ReservationServiceResponse.builder()
					.price(reservationPetSitterService.getPrice())
					.serviceName(reservationPetSitterService.getServiceName())
					.build();
		}

		public static List<ReservationServiceResponse> toList(List<ReservationPetSitterService> reservationPetSitterServiceList){
			return reservationPetSitterServiceList.stream().map(reservationService -> ReservationServiceResponse.builder()
					.price(reservationService.getPrice())
					.serviceName(reservationService.getServiceName())
					.build()).collect(Collectors.toList());
		}
	}
}
