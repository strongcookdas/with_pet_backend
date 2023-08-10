package com.ajou_nice.with_pet.domain.dto.petsitter;


import com.ajou_nice.with_pet.domain.entity.HashTag;
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

	private String hashTagName;


	public static List<PetSitterHashTagInfoResponse> toList(List<HashTag> hashTags){
		return hashTags.stream().map(petSitterHashTag -> PetSitterHashTagInfoResponse.builder()
				.petSitterHashTagId(petSitterHashTag.getHashTagId())
				.hashTagName(petSitterHashTag.getName())
				.build()).collect(Collectors.toList());
	}
}
