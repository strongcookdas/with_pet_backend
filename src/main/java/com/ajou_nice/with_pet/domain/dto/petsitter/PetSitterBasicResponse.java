package com.ajou_nice.with_pet.domain.dto.petsitter;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
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
public class PetSitterBasicResponse {

	private Long petSitterId;

	private String userName;

	private String userId;
	private String userProfileImg;

	private String userPhone;

	private int report_count;


	public static PetSitterBasicResponse of(PetSitter petSitter){
		return PetSitterBasicResponse.builder()
				.petSitterId(petSitter.getId())
				.userName(petSitter.getPetSitterName())
				.userId(petSitter.getUser().getEmail())
				.userProfileImg(petSitter.getProfileImg())
				.userPhone(petSitter.getPetSitterPhone())
				.report_count(petSitter.getReport_count())
				.build();
	}

	public static List<PetSitterBasicResponse> toList(List<PetSitter> petSitters){
		return petSitters.stream().map(petSitter-> PetSitterBasicResponse.builder()
				.petSitterId(petSitter.getId())
				.userName(petSitter.getPetSitterName())
				.userId(petSitter.getUser().getEmail())
				.userProfileImg(petSitter.getProfileImg())
				.userPhone(petSitter.getPetSitterPhone())
				.report_count(petSitter.getReport_count())
				.build()).collect(Collectors.toList());
	}
}
