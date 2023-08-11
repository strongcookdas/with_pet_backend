package com.ajou_nice.with_pet.dto.petsitter;

import com.ajou_nice.with_pet.dto.critical_service.CriticalServiceRequest;
import com.ajou_nice.with_pet.dto.hashtag.HashTagRequest;
import com.ajou_nice.with_pet.dto.house.HouseRequest;
import com.ajou_nice.with_pet.dto.withpet_service.ServiceRequest;
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
public class PetSitterCreateRequest {

    private List<HouseRequest> petSitterHouses;
    private List<HashTagRequest> petSitterHashTags;
    private String petSitterIntroduction;
    private List<ServiceRequest> petSitterServices;
    private List<CriticalServiceRequest> petSitterCriticalServices;
    
}
