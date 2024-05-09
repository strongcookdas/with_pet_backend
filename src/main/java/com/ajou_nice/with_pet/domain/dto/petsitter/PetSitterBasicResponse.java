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

	private Long petsitterId;

	private String petsitterName;

	private String petsitterEmail;
	private String petsitterImg;

	private String petsitterPhone;

	private int reportCount;


	public static PetSitterBasicResponse of(PetSitter petSitter){
		return PetSitterBasicResponse.builder()
				.petsitterId(petSitter.getId())
				.petsitterName(petSitter.getPetSitterName())
				.petsitterEmail(petSitter.getUser().getEmail())
				.petsitterImg(petSitter.getProfileImg())
				.petsitterPhone(petSitter.getPetSitterPhone())
				.reportCount(petSitter.getReportCount())
				.build();
	}

	public static List<PetSitterBasicResponse> toList(List<PetSitter> petSitters){
		return petSitters.stream().map(petSitter-> PetSitterBasicResponse.builder()
				.petsitterId(petSitter.getId())
				.petsitterName(petSitter.getPetSitterName())
				.petsitterEmail(petSitter.getUser().getEmail())
				.petsitterImg(petSitter.getProfileImg())
				.petsitterPhone(petSitter.getPetSitterPhone())
				.reportCount(petSitter.getReportCount())
				.build()).collect(Collectors.toList());
	}
}
