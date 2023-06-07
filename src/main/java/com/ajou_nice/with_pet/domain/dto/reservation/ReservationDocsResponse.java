package com.ajou_nice.with_pet.domain.dto.reservation;

import com.ajou_nice.with_pet.domain.dto.reservation.ReservationCreateResponse.ReservationServiceResponse;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.enums.PayStatus;
import java.time.LocalDateTime;
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
public class ReservationDocsResponse {

	private List<ReservationInfoResponse> waitReservations;
	private List<ReservationInfoResponse> payedReservations;
	private List<ReservationInfoResponse> approveReservations;
	private List<ReservationInfoResponse> useReservations;
	private List<ReservationInfoResponse> doneReservations;

	public static ReservationDocsResponse of(List<Reservation> waitReservations, List<Reservation> payedReservations, List<Reservation> approveReservations,
			List<Reservation> useReservations, List<Reservation> doneReservations){
		return ReservationDocsResponse.builder()
				.waitReservations(ReservationInfoResponse.toList(waitReservations, ""))
				.payedReservations(ReservationInfoResponse.toList(payedReservations, PayStatus.SUCCESS.toString()))
				.approveReservations(ReservationInfoResponse.toList(approveReservations, PayStatus.SUCCESS.toString()))
				.useReservations(ReservationInfoResponse.toList(useReservations, PayStatus.SUCCESS.toString()))
				.doneReservations(ReservationInfoResponse.toList(doneReservations, PayStatus.SUCCESS.toString()))
				.build();
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Getter
	public static class ReservationInfoResponse{
		private String reservationStatus;
		private String petSitterName;
		private LocalDateTime checkIn;
		private LocalDateTime checkOut;
		private int totalCost;
		private String streetAdr;
		private String payStatus;

		private String criticalServiceName;
		private int criticalServicePrice;

		private List<ReservationServiceResponse> reservationServiceResponses;

		public static List<ReservationInfoResponse> toList(List<Reservation> reservations, String payStatus){
			return reservations.stream().map(reservation -> ReservationInfoResponse.builder()
					.reservationStatus(reservation.getReservationStatus().toString())
					.petSitterName(reservation.getPetSitter().getPetSitterName())
					.checkIn(reservation.getCheckIn())
					.checkOut(reservation.getCheckOut())
					.totalCost(reservation.getTotalPrice())
					.streetAdr(reservation.getPetSitter().getPetSitterStreetAdr())
					.payStatus(payStatus)
					.criticalServiceName(reservation.getCriticalServiceName())
					.criticalServicePrice(reservation.getCriticalServicePrice())
					.reservationServiceResponses(ReservationServiceResponse.toList(reservation.getReservationPetSitterServiceList()))
					.build()).collect(Collectors.toList());
		}

	}

}
