package com.ajou_nice.with_pet.withpet_service.model.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.withpet_service.model.dto.PetSitterAddServiceRequest;
import com.ajou_nice.with_pet.withpet_service.model.dto.update.PetSitterUpdateWithPetServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "petsitter_withpetservice")
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
