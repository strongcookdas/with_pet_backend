package com.ajou_nice.with_pet.petsitter.model.dto.register_info;

import com.ajou_nice.with_pet.critical_service.model.dto.add.PetSitterRegisterMyInfoCriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.dto.add.PetSitterRegisterMyInfoPetSitterCriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.hashtag.model.dto.get.PetSitterRegisterMyInfoHashTagResponse;
import com.ajou_nice.with_pet.house.model.dto.get.PetSitterRegisterMyInfoHouseResponse;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.withpet_service.model.dto.add.PetSitterRegisterMyInfoPetSitterWithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.dto.add.PetSitterRegisterMyInfoWithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterRegisterMyInfoResponse {
    private List<PetSitterRegisterMyInfoHouseResponse> petSitterHouses;
    private List<PetSitterRegisterMyInfoHashTagResponse> petSitterHashTags;
    private String petSitterIntroduction;
    private String petSitterLicenseImg;
    private List<PetSitterRegisterMyInfoPetSitterCriticalServiceResponse> petSitterCriticalServices;
    private List<PetSitterRegisterMyInfoCriticalServiceResponse> criticalServices;
    private List<PetSitterRegisterMyInfoPetSitterWithPetServiceResponse> petSitterWithPetServices;
    private List<PetSitterRegisterMyInfoWithPetServiceResponse> withPetServices;

    public static PetSitterRegisterMyInfoResponse of(PetSitter petSitter, List<CriticalService> criticalServiceList, List<WithPetService> withPetServiceList,
                                                     List<PetSitterWithPetService> petSitterWithPetServices, List<PetSitterCriticalService> petSitterCriticalServices){
        return PetSitterRegisterMyInfoResponse.builder()
                .petSitterHouses(PetSitterRegisterMyInfoHouseResponse.toList(petSitter.getPetSitterHouseList()))
                .petSitterHashTags(PetSitterRegisterMyInfoHashTagResponse.toList(petSitter.getPetSitterHashTagList()))
                .petSitterIntroduction(petSitter.getIntroduction())
                .petSitterLicenseImg(petSitter.getUser().getLicenseImg())
                .petSitterCriticalServices(petSitterCriticalServices == null? null :
                        PetSitterRegisterMyInfoPetSitterCriticalServiceResponse.toList(petSitterCriticalServices))
                .criticalServices(PetSitterRegisterMyInfoCriticalServiceResponse.toList(criticalServiceList))
                .petSitterWithPetServices(petSitterWithPetServices == null ? null : PetSitterRegisterMyInfoPetSitterWithPetServiceResponse.toList(petSitterWithPetServices))
                .withPetServices(PetSitterRegisterMyInfoWithPetServiceResponse.toList(withPetServiceList))
                .build();
    }
}
