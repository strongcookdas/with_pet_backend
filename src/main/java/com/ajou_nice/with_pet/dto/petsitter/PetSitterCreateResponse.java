package com.ajou_nice.with_pet.dto.petsitter;

import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.HouseInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse.PetSitterModifyInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterHashTagInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterServiceResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterServiceResponse.PetSitterCriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.Service;
import com.ajou_nice.with_pet.dto.hashtag.HashTagResponse;
import com.ajou_nice.with_pet.dto.house.HouseResponse;
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
    private List<PetSitterServiceResponse> petSitterServices;

    public static PetSitterCreateResponse of(PetSitter petSitter,
            List<PetSitterServiceResponse> petSitterWithPetServices,
            List<PetSitterCriticalServiceResponse> petSitterCriticalServices) {

        return PetSitterCreateResponse.builder()
                .petSitterHouses(HouseResponse.toList(petSitter.getHouseList()))
                .petSitterHashTags(HashTagResponse.toList(petSitter.getHashTagList()))
                .introduction(petSitter.getIntroduction())
                .petSitterLicenseImg(petSitter.getUser().getLicenseImg())
                .petSitterCriticalServices(petSitterCriticalServices)
                .petSitterServices(petSitterWithPetServices)
                .build();

    }
}
