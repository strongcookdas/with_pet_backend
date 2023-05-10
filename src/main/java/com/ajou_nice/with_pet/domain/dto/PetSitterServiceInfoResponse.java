package com.ajou_nice.with_pet.domain.dto;


import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterServiceInfoResponse {

	private Long petSitterServiceId;

	private WithPetServiceResponse petSitterService;

	private int price;

	/*
	public static PetSitterServiceInfoResponse of(PetSitter petSitter){
		return PetSitterServiceInfoResponse.builder()
				.petSitterService(petsitter.get)
	}

	 */
	public static List<PetSitterServiceInfoResponse> toList(List<PetSitterWithPetService> petSitterServiceList){
		return petSitterServiceList.stream().map(petSitterService -> PetSitterServiceInfoResponse.builder()
				.petSitterServiceId(petSitterService.getId())
				.petSitterService(WithPetServiceResponse.of(petSitterService.getWithPetService()))
				.price(petSitterService.getPrice())
				.build()).collect(Collectors.toList());
	}

}
