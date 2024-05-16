package com.ajou_nice.with_pet.petsitter.model.dto.my_info;

import com.ajou_nice.with_pet.critical_service.model.dto.CriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.hashtag.model.dto.PetSitterHashTagInfoResponse;
import com.ajou_nice.with_pet.house.model.dto.HouseInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterServiceResponse;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.withpet_service.model.dto.WithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import lombok.*;

import java.util.List;

;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterMyInfoResponse {
    private List<HouseInfoResponse> petSitterHouses;
    private List<PetSitterHashTagInfoResponse> petSitterHashTags;
    private String introduction;
    private String petSitterLicenseImg;

    private List<PetSitterServiceResponse.PetSitterCriticalServiceResponse> petSitterCriticalServices;
    private List<CriticalServiceResponse> criticalServices;
    private List<PetSitterServiceResponse> petSitterServices;
    private List<WithPetServiceResponse> withPetServices;

    public static PetSitterMyInfoResponse of(PetSitter petSitter, List<CriticalService> criticalServiceList, List<WithPetService> withPetServiceList,
                                                                         List<PetSitterWithPetService> petSitterWithPetServices, List<PetSitterCriticalService> petSitterCriticalServices) {
        return PetSitterMyInfoResponse.builder()
                .petSitterHouses(HouseInfoResponse.toList(petSitter.getPetSitterHouseList()))
                .petSitterHashTags(PetSitterHashTagInfoResponse.toList(petSitter.getPetSitterHashTagList()))
                .introduction(petSitter.getIntroduction())
                .petSitterLicenseImg(petSitter.getUser().getLicenseImg())
                .petSitterCriticalServices(petSitterCriticalServices == null ? null :
                        PetSitterServiceResponse.PetSitterCriticalServiceResponse.toList(petSitterCriticalServices))
                .criticalServices(CriticalServiceResponse.toList(criticalServiceList))
                .petSitterServices(petSitterWithPetServices == null ? null : PetSitterServiceResponse.toList(petSitterWithPetServices))
                .withPetServices(WithPetServiceResponse.toList(withPetServiceList))
                .build();
    }
}
