package com.ajou_nice.with_pet.domain.dto.petsitter;


import com.ajou_nice.with_pet.domain.entity.PetSitter;
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
public class PetSitterMainResponse {

	private String userName;

	private Long petSitterId;

	private String petSitterRepresentativeHouse;

	private String introduction;

	public static PetSitterMainResponse of(PetSitter petSitter){
		return PetSitterMainResponse.builder()
				.petSitterId(petSitter.getId())
				.petSitterRepresentativeHouse(petSitter.getPetSitterHouseList()
						.stream().filter(house->house.getRepresentative().equals("true")).findAny().orElse(null)
						.getHouse_img())
				.userName(petSitter.getApplicant().getUser().getName())
				.introduction(petSitter.getIntroduction())
				.build();
	}
}
