package com.ajou_nice.with_pet.petsitter.model.dto.get_detail_info;


import com.ajou_nice.with_pet.critical_service.model.dto.get.PetSitterGetDetailInfoCriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.Review;
import com.ajou_nice.with_pet.hashtag.model.dto.get.PetSitterGetDetailInfoHashTagResponse;
import com.ajou_nice.with_pet.house.model.dto.get.PetSitterGetDetailInfoHouseResponse;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.review.model.dto.get.PetSitterGetDetailInfoReviewResponse;
import com.ajou_nice.with_pet.withpet_service.model.dto.get.PetSitterGetDetailInfoWithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.entity.PetSitterWithPetService;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterGetDetailInfoResponse {
	private Long petSitterId;
	private Long petSitterUserId;
	private String petSitterName;
	private String petSitterProfileImg;
	private String petSitterAddress;
	private List<PetSitterGetDetailInfoHashTagResponse> petSitterHashTags;
	private List<PetSitterGetDetailInfoHouseResponse> petSitterHouses;
	private List<PetSitterGetDetailInfoWithPetServiceResponse> petSitterWithPetServices;
	private List<PetSitterGetDetailInfoCriticalServiceResponse> petSitterCriticalServices;
	private String petSitterIntroduction;
	private String petSitterLicenseImg;
	private List<PetSitterGetDetailInfoReviewResponse> petSitterReviews;

	public static PetSitterGetDetailInfoResponse of(PetSitter petSitter, List<Review> reviews, List<PetSitterWithPetService> petSitterWithPetServices,
													List<PetSitterCriticalService> petSitterCriticalServices){
		return PetSitterGetDetailInfoResponse.builder()
				.petSitterId(petSitter.getId())
				.petSitterUserId(petSitter.getUser().getId())
				.petSitterName(petSitter.getUser().getName())
				.petSitterProfileImg(petSitter.getUser().getProfileImg())
				.petSitterAddress(petSitter.getUser().getAddress().getStreetAdr())
				.petSitterHashTags(PetSitterGetDetailInfoHashTagResponse.toList(petSitter.getPetSitterHashTagList()))
				.petSitterHouses(PetSitterGetDetailInfoHouseResponse.toList(petSitter.getPetSitterHouseList()))
				.petSitterWithPetServices(petSitterWithPetServices == null? null : PetSitterGetDetailInfoWithPetServiceResponse.toList(petSitterWithPetServices))
				.petSitterCriticalServices(petSitterCriticalServices == null? null :
						PetSitterGetDetailInfoCriticalServiceResponse.toList(petSitterCriticalServices))
				.petSitterIntroduction(petSitter.getIntroduction())
				.petSitterLicenseImg(petSitter.getUser().getLicenseImg())
				.petSitterReviews(reviews == null ? null : PetSitterGetDetailInfoReviewResponse.toList(reviews))
				.build();
	}
}
