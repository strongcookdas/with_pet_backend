package com.ajou_nice.with_pet.petsitter.create;

import static org.mockito.Mockito.mock;

import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.service.hashtag.HashTagService;
import com.ajou_nice.with_pet.service.house.HouseService;
import com.ajou_nice.with_pet.service.petsitter.PetSitterCreateService;
import com.ajou_nice.with_pet.service.petsitter_critical_service.PetSitterCriticalServiceCreateService;
import com.ajou_nice.with_pet.service.petsitter_withpet_service.PetSitterWithPetServiceCreateService;
import com.ajou_nice.with_pet.service.petsitter_withpet_service.PetSitterWithPetServiceService;

public class PetSitterCreateServiceTest {

    PetSitterCreateService petSitterCreateService;

    HouseService houseService = mock(HouseService.class);
    HashTagService hashTagService = mock(HashTagService.class);
    PetSitterWithPetServiceCreateService petSitterWithPetServiceCreateService = mock(PetSitterWithPetServiceCreateService.class);
    PetSitterCriticalServiceCreateService petSitterCriticalServiceCreateService = mock(PetSitterCriticalServiceCreateService.class);

}
