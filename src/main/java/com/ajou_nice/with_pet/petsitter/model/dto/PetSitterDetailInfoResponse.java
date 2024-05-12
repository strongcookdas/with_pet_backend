package com.ajou_nice.with_pet.petsitter.model.dto;


import com.ajou_nice.with_pet.critical_service.model.dto.CriticalServiceResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterServiceResponse.PetSitterCriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.review.ReviewResponse;
import com.ajou_nice.with_pet.withpet_service.model.dto.WithPetServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.Review;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
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
public class PetSitterDetailInfoResponse { //펫시터 상세정보 response

	private Long petSitterId;

	private Long petSitterUserId;

	private String petSitterName;

	private String petSitterProfileImg;

	private String petSitterAddress;

	private List<PetSitterHashTagInfoResponse> petSitterHashTags;

	private List<HouseInfoResponse> petSitterHouses;

	private List<PetSitterServiceResponse> petSitterServices;

	private List<PetSitterCriticalServiceResponse> petSitterCriticalServices;

	private String introduction;

	private String petSitterLicenseImg;

	private List<ReviewResponse> reviewResponses;

	public static PetSitterDetailInfoResponse of(PetSitter petSitter, List<Review> reviews, List<PetSitterWithPetService> petSitterWithPetServices,
			List<PetSitterCriticalService> petSitterCriticalServices){
		return PetSitterDetailInfoResponse.builder()
				.petSitterId(petSitter.getId())
				.petSitterUserId(petSitter.getUser().getId())
				.petSitterName(petSitter.getUser().getName())
				.petSitterProfileImg(petSitter.getUser().getProfileImg())
				.petSitterAddress(petSitter.getUser().getAddress().getStreetAdr())
				.petSitterHashTags(PetSitterHashTagInfoResponse.toList(petSitter.getPetSitterHashTagList()))
				.petSitterHouses(HouseInfoResponse.toList(petSitter.getPetSitterHouseList()))
				.petSitterServices(petSitterWithPetServices == null? null : PetSitterServiceResponse.toList(petSitterWithPetServices))
				.petSitterCriticalServices(petSitterCriticalServices == null? null :
						PetSitterCriticalServiceResponse.toList(petSitterCriticalServices))
				.introduction(petSitter.getIntroduction())
				.petSitterLicenseImg(petSitter.getUser().getLicenseImg())
				.reviewResponses(reviews == null ? null : ReviewResponse.toList(reviews))
				.build();
	}
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Builder
	@ToString
	public static class PetSitterModifyInfoResponse {

		private List<HouseInfoResponse> petSitterHouses;
		private List<PetSitterHashTagInfoResponse> petSitterHashTags;
		private String introduction;
		private String petSitterLicenseImg;

		private List<PetSitterCriticalServiceResponse> petSitterCriticalServices;
		private List<CriticalServiceResponse> criticalServices;
		private List<PetSitterServiceResponse> petSitterServices;
		private List<WithPetServiceResponse> withPetServices;

		public static PetSitterModifyInfoResponse of(PetSitter petSitter, List<CriticalService> criticalServiceList,  List<WithPetService> withPetServiceList,
				List<PetSitterWithPetService> petSitterWithPetServices, List<PetSitterCriticalService> petSitterCriticalServices){
			return PetSitterModifyInfoResponse.builder()
					.petSitterHouses(HouseInfoResponse.toList(petSitter.getPetSitterHouseList()))
					.petSitterHashTags(PetSitterHashTagInfoResponse.toList(petSitter.getPetSitterHashTagList()))
					.introduction(petSitter.getIntroduction())
					.petSitterLicenseImg(petSitter.getUser().getLicenseImg())
					.petSitterCriticalServices(petSitterCriticalServices == null? null :
							PetSitterCriticalServiceResponse.toList(petSitterCriticalServices))
					.criticalServices(CriticalServiceResponse.toList(criticalServiceList))
					.petSitterServices(petSitterWithPetServices == null ? null : PetSitterServiceResponse.toList(petSitterWithPetServices))
					.withPetServices(WithPetServiceResponse.toList(withPetServiceList))
					.build();
		}
	}
}
