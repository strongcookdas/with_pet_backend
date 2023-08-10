package com.ajou_nice.with_pet.domain.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
	@JoinColumn(name = "petSitterId")
	private PetSitter petSitter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ServiceId")
	private Service service;

	private int price;

	public void changeServicePrice(int price){
		this.price = price;
	}


	public static PetSitterWithPetService toEntity(Service service, PetSitter petSitter, int price){
		return PetSitterWithPetService.builder()
				.petSitter(petSitter)
				.price(price)
				.service(service)
				.build();
	}
}
