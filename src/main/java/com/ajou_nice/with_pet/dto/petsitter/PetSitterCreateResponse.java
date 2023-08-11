package com.ajou_nice.with_pet.dto.petsitter;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.dto.hashtag.HashTagResponse;
import com.ajou_nice.with_pet.dto.house.HouseResponse;
import com.ajou_nice.with_pet.dto.petsitter_withpet_service.PetSitterWithPetServiceCreateResponse;
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
public class PetSitterCreateResponse {

    private List<HouseResponse> petSitterHouses;
    private List<HashTagResponse> petSitterHashTags;
    private String introduction;
    private String petSitterLicenseImg;

    private List<PetSitterCriticalServiceResponse> petSitterCriticalServices;
    private List<PetSitterWithPetServiceCreateResponse> petSitterServices;

    public static PetSitterCreateResponse of(PetSitter petSitter,
            List<HouseResponse> houseResponses,
            List<PetSitterWithPetServiceCreateResponse> petSitterWithPetServices,
            List<PetSitterCriticalServiceResponse> petSitterCriticalServices) {

        return PetSitterCreateResponse.builder()
                .petSitterHouses(houseResponses)
                .petSitterHashTags(HashTagResponse.toList(petSitter.getHashTagList()))
                .introduction(petSitter.getIntroduction())
                .petSitterLicenseImg(petSitter.getUser().getLicenseImg())
                .petSitterCriticalServices(petSitterCriticalServices)
                .petSitterServices(petSitterWithPetServices)
                .build();

    }
}
