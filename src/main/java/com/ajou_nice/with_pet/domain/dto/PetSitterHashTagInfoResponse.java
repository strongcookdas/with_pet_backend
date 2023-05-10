package com.ajou_nice.with_pet.domain.dto;


import com.ajou_nice.with_pet.domain.entity.PetSitterHashTag;
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
public class PetSitterHashTagInfoResponse {

	private Long petSitterHashTagId;

	private HashTagInfoResponse petSitterHashTag;


	public static List<PetSitterHashTagInfoResponse> toList(List<PetSitterHashTag> petSitterHashTags){
		return petSitterHashTags.stream().map(petSitterHashTag -> PetSitterHashTagInfoResponse.builder()
				.petSitterHashTagId(petSitterHashTag.getId())
				.petSitterHashTag(HashTagInfoResponse.of(petSitterHashTag.getHashTag()))
				.build()).collect(Collectors.toList());
	}
}
