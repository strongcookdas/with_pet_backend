package com.ajou_nice.with_pet.domain.dto.petsitter;


import com.ajou_nice.with_pet.domain.dto.PetSitterHashTagInfoResponse;
import com.ajou_nice.with_pet.domain.dto.PetSitterServiceInfoResponse;
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
public class PetSitterMainDto {

	private String userName;

	//
	private String introduction;

	private Long petSitterId;

	private String mainHouseImg;

	private List<PetSitterServiceInfoResponse> petSitterServices;

	private List<PetSitterHashTagInfoResponse> petSitterHashTags;



}
