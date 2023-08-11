package com.ajou_nice.with_pet.service.petsitter_critical_service;

import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.dto.critical_service.CriticalServiceRequest;
import com.ajou_nice.with_pet.dto.petsitter_critical_service.PetSitterCriticalServiceCreateResponse;
import com.ajou_nice.with_pet.service.critical_service.CriticalServiceService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetSitterCriticalServiceCreateService {

    private final CriticalServiceService criticalServiceService;
    private final PetSitterCriticalServiceService petSitterCriticalServiceService;

    @Transactional
    public List<PetSitterCriticalServiceCreateResponse> registerPetSitterCriticalService(
            PetSitter petSitter, List<CriticalServiceRequest> criticalServiceRequestList) {

        List<PetSitterCriticalService> petSitterCriticalServices = new ArrayList<>();

        for (CriticalServiceRequest criticalServiceRequest : criticalServiceRequestList) {
            CriticalService criticalService = criticalServiceService.getCriticalService(
                    criticalServiceRequest.getCriticalServiceId());
            PetSitterCriticalService petSitterCriticalService = petSitterCriticalServiceService.registerPetSitterCriticalService(
                    petSitter, criticalService, criticalServiceRequest);
            petSitterCriticalServices.add(petSitterCriticalService);
        }

        List<CriticalService> criticalServices = criticalServiceService.getCriticalServices();
        List<PetSitterCriticalServiceCreateResponse> petSitterCriticalServiceCreateResponses = new ArrayList<>();

        for (CriticalService criticalService : criticalServices) {
            boolean check = false;
            for (PetSitterCriticalService petSitterCriticalService : petSitterCriticalServices) {
                if (petSitterCriticalService.getCriticalService().getCriticalServiceId()
                        == criticalService.getCriticalServiceId()) {
                    petSitterCriticalServiceCreateResponses.add(PetSitterCriticalServiceCreateResponse.of(petSitterCriticalService));
                    check = true;
                    break;
                }
            }
            if (!check) {
                petSitterCriticalServiceCreateResponses.add(
                        PetSitterCriticalServiceCreateResponse.of(criticalService));
            }

        }

        return petSitterCriticalServiceCreateResponses;
    }
}
