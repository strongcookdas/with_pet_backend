package com.ajou_nice.with_pet.domain.dto.petsitter;


import com.ajou_nice.with_pet.domain.dto.HashTagInfoResponse;
import com.ajou_nice.with_pet.domain.dto.HouseInfoResponse;
import com.ajou_nice.with_pet.domain.dto.PetSitterHashTagInfoResponse;
import com.ajou_nice.with_pet.domain.dto.PetSitterServiceInfoResponse;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.entity.HashTag;
import com.ajou_nice.with_pet.domain.entity.House;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterHashTag;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import java.util.List;
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
public class PetSitterInfoResponse {

	private Long petSitterId;

	private List<PetSitterServiceInfoResponse> petSitterServices;

	private List<HouseInfoResponse> petSitterHouses;
	private List<PetSitterHashTagInfoResponse> petSitterHashTags;

	private String introduction;

	public static PetSitterInfoResponse of(PetSitter petSitter){
		return PetSitterInfoResponse.builder()
				.petSitterId(petSitter.getId())
				.petSitterServices(PetSitterServiceInfoResponse.toList(petSitter.getPetSitterWithPetServiceList()))
				.petSitterHouses(HouseInfoResponse.toList(petSitter.getPetSitterHouseList()))
				.petSitterHashTags(PetSitterHashTagInfoResponse.toList(petSitter.getPetSitterHashTagList()))
				.build();
	}
}
