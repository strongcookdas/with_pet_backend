package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.dto.critical_service.CriticalServiceRequest;
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
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "petsitter_criticalservice")
public class PetSitterCriticalService {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "petSitterId")
	private PetSitter petSitter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "criticalServiceId")
	private CriticalService criticalService;

	private int price;

	public static PetSitterCriticalService toEntity(CriticalService criticalService, PetSitter petSitter, int price){
		return PetSitterCriticalService.builder()
				.petSitter(petSitter)
				.price(price)
				.criticalService(criticalService)
				.build();
	}

	public static PetSitterCriticalService of(PetSitter petSitter,CriticalService criticalService, CriticalServiceRequest criticalServiceRequest){
		return PetSitterCriticalService.builder()
				.petSitter(petSitter)
				.price(criticalServiceRequest.getPrice())
				.criticalService(criticalService)
				.build();
	}
}
