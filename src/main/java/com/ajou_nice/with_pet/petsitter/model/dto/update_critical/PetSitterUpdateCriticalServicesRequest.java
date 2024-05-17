package com.ajou_nice.with_pet.petsitter.model.dto.update_critical;

import com.ajou_nice.with_pet.critical_service.model.dto.update.PetSitterUpdateCriticalServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PetSitterUpdateCriticalServicesRequest {
    private List<PetSitterUpdateCriticalServiceRequest> petSitterCriticalServiceRequests;

}
