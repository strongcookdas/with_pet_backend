package com.ajou_nice.with_pet.domain.dto.reservation;

import com.ajou_nice.with_pet.domain.dto.reservation.ReservationCreateResponse.ReservationServiceResponse;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.enums.PayStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Lob;
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
	@ToString
	public static class ReservationInfoResponse{

		private Long reservationId;
		private String reservationStatus;
		private String petSitterName;
		private String dogName;
		@Lob
		private String dogImg;
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
					.reservationId(reservation.getReservationId())
					.reservationStatus(reservation.getReservationStatus().toString())
					.petSitterName(reservation.getPetSitter().getPetSitterName())
					.dogName(reservation.getDog().getDogName())
					.dogImg(reservation.getDog().getDogProfileImg())
					.checkIn(reservation.getCheckIn())
					.checkOut(reservation.getCheckOut())
					.totalCost(reservation.getTotalPrice())
					.streetAdr(reservation.getPetSitter().getPetSitterStreetAdr())
					.payStatus(payStatus)
					.criticalServiceName(reservation.getCriticalServiceName())
					.criticalServicePrice(reservation.getCriticalServicePrice())
					.reservationServiceResponses(reservation.getReservationPetSitterServiceList() == null ?
							null : ReservationServiceResponse.toList(reservation.getReservationPetSitterServiceList()))
					.build()).collect(Collectors.toList());
		}

	}

}
