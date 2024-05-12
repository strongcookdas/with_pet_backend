package com.ajou_nice.with_pet.petsitter.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
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
@Table(name = "petsitter_criticalservice")
public class PetSitterCriticalService {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "petsitter_id")
	private PetSitter petSitter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "critical_serviceId")
	private CriticalService criticalService;

	private int price;

	public static PetSitterCriticalService toEntity(CriticalService criticalService, PetSitter petSitter, int price){
		return PetSitterCriticalService.builder()
				.petSitter(petSitter)
				.price(price)
				.criticalService(criticalService)
				.build();
	}
}
