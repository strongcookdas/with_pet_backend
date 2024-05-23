package com.ajou_nice.with_pet.petsitter.model.dto.register_info;

import com.ajou_nice.with_pet.critical_service.model.dto.add.PetSitterRegisterMyInfoCriticalServiceRequest;
import com.ajou_nice.with_pet.hashtag.model.dto.add.PetSitterRegisterMyInfoHashTagRequest;
import com.ajou_nice.with_pet.house.model.dto.add.PetSitterRegisterMyInfoHouseRequest;
import com.ajou_nice.with_pet.withpet_service.model.dto.add.PetSitterRegisterMyInfoWithPetServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Lob;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterRegisterMyInfoRequest {

    private List<PetSitterRegisterMyInfoHouseRequest> petSitterHouses;
    private List<PetSitterRegisterMyInfoHashTagRequest> petSitterHashTags;
    @Lob
    private String petSitterIntroduction;
    private List<PetSitterRegisterMyInfoWithPetServiceRequest> petSitterWithPetServices;
    private List<PetSitterRegisterMyInfoCriticalServiceRequest> petSitterCriticalServices;
}
