package com.ajou_nice.with_pet.domain.dto.reservation;


import com.ajou_nice.with_pet.domain.dto.reservation.ReservationCreateResponse.ReservationServiceResponse;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PaymentResponseForPetSitter {

	private String userName;
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

	public static PaymentResponseForPetSitter of(Reservation reservation){
		return PaymentResponseForPetSitter.builder()
				.userName(reservation.getUser().getName())
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
				.build();
	}
}
