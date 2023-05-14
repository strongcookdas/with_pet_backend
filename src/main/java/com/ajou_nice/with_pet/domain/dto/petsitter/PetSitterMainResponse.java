package com.ajou_nice.with_pet.domain.dto.petsitter;


import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
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

	private int review_count;
	private Double star_rate;

	public static PetSitterMainResponse of(PetSitter petSitter){
		return PetSitterMainResponse.builder()
				.petSitterId(petSitter.getId())
				.petSitterRepresentativeHouse(petSitter.getPetSitterHouseList()
						.stream().filter(house->house.getRepresentative().equals(true)).findAny().orElseThrow(()->{
							throw new AppException(ErrorCode.PETSITTER_MAIN_HOUSE_NOT_FOUND,
									ErrorCode.PETSITTER_MAIN_HOUSE_NOT_FOUND.getMessage());
						})
						.getHouse_img())
				.userName(petSitter.getApplicant().getUser().getName())
				.review_count(petSitter.getReview_count())
				.star_rate(petSitter.getStar_rate())
				.build();
	}
}
