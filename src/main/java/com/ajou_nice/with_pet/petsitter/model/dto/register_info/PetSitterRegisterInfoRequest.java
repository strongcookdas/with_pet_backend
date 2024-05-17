package com.ajou_nice.with_pet.petsitter.model.dto.register_info;

import com.ajou_nice.with_pet.critical_service.model.dto.add.PetSitterAddCriticalServiceRequest;
import com.ajou_nice.with_pet.hashtag.model.dto.add.PetSitterAddHashTagRequest;
import com.ajou_nice.with_pet.house.model.dto.PetSitterAddHouseRequest;
import com.ajou_nice.with_pet.withpet_service.model.dto.PetSitterAddServiceRequest;
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
public class PetSitterRegisterInfoRequest {

    private List<PetSitterAddHouseRequest> petSitterHouseRequests;
    private List<PetSitterAddHashTagRequest> petSitterHashTagRequests;

    @Lob
    private String petSitterIntroduction;
    private List<PetSitterAddServiceRequest> petSitterServiceRequests;
    private List<PetSitterAddCriticalServiceRequest> petSitterCriticalServiceRequests;
}
