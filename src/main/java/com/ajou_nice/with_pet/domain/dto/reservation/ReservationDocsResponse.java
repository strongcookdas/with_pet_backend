package com.ajou_nice.with_pet.domain.dto.reservation;

import com.ajou_nice.with_pet.domain.entity.Reservation;
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
public class ReservationDocsResponse {

	private List<ReservationInfoResponse> waitReservations;
	private List<ReservationInfoResponse> payedReservations;
	private List<ReservationInfoResponse> approveReservations;
	private List<ReservationInfoResponse> useReservations;
	private List<ReservationInfoResponse> doneReservations;


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

	}

}
