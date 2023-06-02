package com.ajou_nice.with_pet.domain.dto.petsitter;


import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterServiceResponse.PetSitterCriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
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
public class PetSitterDetailInfoResponse { //펫시터 상세정보 response

	private Long petSitterId;

	private String petSitterName;

	private String petSitterProfileImg;

	private String petSitterAddress;

	private List<PetSitterHashTagInfoResponse> petSitterHashTags;

	private List<HouseInfoResponse> petSitterHouses;

	private List<PetSitterServiceResponse> petSitterServices;

	private List<PetSitterCriticalServiceResponse> petSitterCriticalServices;

	private String introduction;

	private String petSitterLicenseImg;

	public static PetSitterDetailInfoResponse of(PetSitter petSitter){
		return PetSitterDetailInfoResponse.builder()
				.petSitterId(petSitter.getId())
				.petSitterName(petSitter.getPetSitterName())
				.petSitterProfileImg(petSitter.getProfileImg())
				.petSitterAddress(petSitter.getPetSitterStreetAdr())
				.petSitterHashTags(PetSitterHashTagInfoResponse.toList(petSitter.getPetSitterHashTagList()))
				.petSitterHouses(HouseInfoResponse.toList(petSitter.getPetSitterHouseList()))
				.petSitterServices(PetSitterServiceResponse.toList(petSitter.getPetSitterWithPetServiceList()))
				.petSitterCriticalServices(PetSitterCriticalServiceResponse.toList(petSitter.getPetSitterCriticalServiceList()))
				.introduction(petSitter.getIntroduction())
				.petSitterLicenseImg(petSitter.getPetSitterLicenseImg())
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

		public static PetSitterModifyInfoResponse of(PetSitter petSitter, List<CriticalService> criticalServiceList,  List<WithPetService> withPetServiceList){
			return PetSitterModifyInfoResponse.builder()
					.petSitterHouses(HouseInfoResponse.toList(petSitter.getPetSitterHouseList()))
					.petSitterHashTags(PetSitterHashTagInfoResponse.toList(petSitter.getPetSitterHashTagList()))
					.introduction(petSitter.getIntroduction())
					.petSitterLicenseImg(petSitter.getPetSitterLicenseImg())
					.petSitterCriticalServices(PetSitterCriticalServiceResponse.toList(petSitter.getPetSitterCriticalServiceList()))
					.criticalServices(CriticalServiceResponse.toList(criticalServiceList))
					.petSitterServices(PetSitterServiceResponse.toList(petSitter.getPetSitterWithPetServiceList()))
					.withPetServices(WithPetServiceResponse.toList(withPetServiceList))
					.build();
		}
	}
}
