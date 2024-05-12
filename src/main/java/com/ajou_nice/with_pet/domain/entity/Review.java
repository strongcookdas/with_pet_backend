package com.ajou_nice.with_pet.domain.entity;


import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
public class Review{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservationId")
	private Reservation reservation;

	private Double grade;

	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "petsitter_id")
	private PetSitter petSitter;

	@Lob
	private String content;

	public static Review of(Reservation reservation, Double grade, String content){
		return Review.builder()
				.reservation(reservation)
				.grade(grade)
				.content(content)
				.createdAt(LocalDateTime.now())
				.petSitter(reservation.getPetSitter())
				.build();
	}
}
