package com.ajou_nice.with_pet.domain.dto.petsitter;


import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
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
public class PetSitterServiceResponse {

	private Long petSitterServiceId;

	private int price;

	public static List<PetSitterServiceResponse> toList(List<PetSitterWithPetService> petSitterServiceList){
		return petSitterServiceList.stream().map(petSitterService -> PetSitterServiceResponse.builder()
				.petSitterServiceId(petSitterService.getId())
				.price(petSitterService.getPrice())
				.build()).collect(Collectors.toList());
	}

}
