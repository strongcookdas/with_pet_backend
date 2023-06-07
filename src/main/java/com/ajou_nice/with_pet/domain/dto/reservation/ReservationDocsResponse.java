package com.ajou_nice.with_pet.domain.dto.reservation;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReservationDocsResponse {

	private String name;

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
