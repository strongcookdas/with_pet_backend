package com.ajou_nice.with_pet.domain.entity;


import com.ajou_nice.with_pet.domain.dto.PetSitterServiceInfoResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterWithPetService {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "petsitter_id")
	private PetSitter petSitter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	private WithPetService withPetService;

	private int price;


	public static PetSitterWithPetService toEntity(WithPetService withPetService, PetSitter petSitter, int price){
		return PetSitterWithPetService.builder()
				.petSitter(petSitter)
				.price(price)
				.withPetService(withPetService)
				.build();
	}
}
