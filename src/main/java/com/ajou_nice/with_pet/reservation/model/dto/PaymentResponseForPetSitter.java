package com.ajou_nice.with_pet.reservation.model.dto;


import com.ajou_nice.with_pet.reservation.model.dto.ReservationCreateResponse.ReservationServiceResponse;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import java.util.List;

import com.ajou_nice.with_pet.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PaymentResponseForPetSitter {

	private String checkIn;
	private String checkOut;
	private String userName;
	private String dogName;
	private Gender dogGender;
	private String dogProfileImg;
	private String dogBreed;
	private Float dogWeight;
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
				.checkIn(reservation.getReservationCheckIn().toString())
				.checkOut(reservation.getReservationCheckOut().toString())
				.userName(reservation.getUser().getName())
				.dogName(reservation.getDog().getDogName())
				.dogGender(reservation.getDog().getDogGender())
				.dogProfileImg(reservation.getDog().getDogProfileImg())
				.dogBreed(reservation.getDog().getDogBreed())
				.dogIsbn(reservation.getDog().getDogIsbn())
				.dogWeight(reservation.getDog().getDogWeight())
				.dogNeutralization(reservation.getDog().getDogNeutralization())
				.dogSocializationTemperature(reservation.getDog().getDogSocializationTemperature())
				.dogSocializationDegree(reservation.getDog().getDogSocializationDegree())
				.dogAffectionTemperature(reservation.getDog().getDogAffectionTemperature())
				.totalCost(reservation.getReservationTotalPrice())
				.criticalServiceName(reservation.getCriticalServiceName())
				.criticalServicePrice(reservation.getCriticalServicePrice())
				.reservationServiceResponses(reservation.getReservationPetSitterServiceList() == null ?
						null : ReservationServiceResponse.toList(reservation.getReservationPetSitterServiceList()))
				.build();
	}
}
