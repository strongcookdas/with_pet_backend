package com.ajou_nice.with_pet.petsitter.model.dto.update_service;

import com.ajou_nice.with_pet.withpet_service.model.dto.update.PetSitterUpdateWithPetServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PetSitterUpdateWithPetServicesRequest {
    private List<PetSitterUpdateWithPetServiceRequest> petSitterServiceRequests;

}