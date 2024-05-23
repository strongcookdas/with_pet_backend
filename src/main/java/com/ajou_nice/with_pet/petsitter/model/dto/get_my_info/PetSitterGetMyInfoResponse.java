package com.ajou_nice.with_pet.petsitter.model.dto.get_my_info;

import com.ajou_nice.with_pet.critical_service.model.dto.get.PetSitterGetMyInfoCriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.dto.get.PetSitterGetMyInfoPetSitterCriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.hashtag.model.dto.get.PetSitterGetMyInfoHashTagResponse;
import com.ajou_nice.with_pet.house.model.dto.get.PetSitterGetMyInfoHouseResponse;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.withpet_service.model.dto.get.PetSitterGetMyInfoPetSitterWithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.dto.get.PetSitterGetMyInfoWithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterGetMyInfoResponse {
    private List<PetSitterGetMyInfoHouseResponse> petSitterHouses;
    private List<PetSitterGetMyInfoHashTagResponse> petSitterHashTags;
    private String petSitterIntroduction;
    private String petSitterLicenseImg;
    private List<PetSitterGetMyInfoPetSitterCriticalServiceResponse> petSitterCriticalServices;
    private List<PetSitterGetMyInfoCriticalServiceResponse> criticalServices;
    private List<PetSitterGetMyInfoPetSitterWithPetServiceResponse> petSitterWithPetServices;
    private List<PetSitterGetMyInfoWithPetServiceResponse> withPetServices;

    public static PetSitterGetMyInfoResponse of(PetSitter petSitter, List<CriticalService> criticalServiceList, List<WithPetService> withPetServiceList,
                                                List<PetSitterWithPetService> petSitterWithPetServices, List<PetSitterCriticalService> petSitterCriticalServices) {
        return PetSitterGetMyInfoResponse.builder()
                .petSitterHouses(PetSitterGetMyInfoHouseResponse.toList(petSitter.getPetSitterHouseList()))
                .petSitterHashTags(PetSitterGetMyInfoHashTagResponse.toList(petSitter.getPetSitterHashTagList()))
                .petSitterIntroduction(petSitter.getIntroduction())
                .petSitterLicenseImg(petSitter.getUser().getLicenseImg())
                .petSitterCriticalServices(petSitterCriticalServices == null ? null :
                        PetSitterGetMyInfoPetSitterCriticalServiceResponse.toList(petSitterCriticalServices))
                .criticalServices(PetSitterGetMyInfoCriticalServiceResponse.toList(criticalServiceList))
                .petSitterWithPetServices(petSitterWithPetServices == null ? null : PetSitterGetMyInfoPetSitterWithPetServiceResponse.toList(petSitterWithPetServices))
                .withPetServices(PetSitterGetMyInfoWithPetServiceResponse.toList(withPetServiceList))
                .build();
    }
}
