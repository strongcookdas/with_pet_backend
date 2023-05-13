package com.ajou_nice.with_pet.domain.dto.petsitter;


import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
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

	private AddressDto petSitterAddress;

	private List<PetSitterHashTagInfoResponse> petSitterHashTags;

	private List<HouseInfoResponse> petSitterHouses;

	private List<PetSitterServiceResponse> petSitterServices;

	private String introduction;

	private String petSitterLicenseImg;

	public static PetSitterDetailInfoResponse of(PetSitter petSitter){
		return PetSitterDetailInfoResponse.builder()
				.petSitterId(petSitter.getId())
				.petSitterAddress(AddressDto.of(petSitter.getApplicant().getUser().getAddress()))
				.petSitterHashTags(PetSitterHashTagInfoResponse.toList(petSitter.getPetSitterHashTagList()))
				.petSitterHouses(HouseInfoResponse.toList(petSitter.getPetSitterHouseList()))
				.petSitterServices(PetSitterServiceResponse.toList(petSitter.getPetSitterWithPetServiceList()))
				.introduction(petSitter.getIntroduction())
				.petSitterLicenseImg(petSitter.getApplicant().getLicense_img())
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

		private List<PetSitterServiceResponse> petSitterServices;
		private List<WithPetServiceResponse> withPetServices;

		public static PetSitterModifyInfoResponse of(PetSitter petSitter, List<WithPetService> withPetServiceList){
			return PetSitterModifyInfoResponse.builder()
					.petSitterHouses(HouseInfoResponse.toList(petSitter.getPetSitterHouseList()))
					.petSitterHashTags(PetSitterHashTagInfoResponse.toList(petSitter.getPetSitterHashTagList()))
					.introduction(petSitter.getIntroduction())
					.petSitterLicenseImg(petSitter.getApplicant().getLicense_img())
					.petSitterServices(PetSitterServiceResponse.toList(petSitter.getPetSitterWithPetServiceList()))
					.withPetServices(WithPetServiceResponse.toList(withPetServiceList))
					.build();
		}
	}
}
