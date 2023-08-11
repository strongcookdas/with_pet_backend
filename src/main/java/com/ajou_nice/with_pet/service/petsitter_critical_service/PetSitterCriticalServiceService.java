package com.ajou_nice.with_pet.service.petsitter_critical_service;

import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.dto.critical_service.CriticalServiceRequest;
import com.ajou_nice.with_pet.repository.PetSitterCriticalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetSitterCriticalServiceService {

    private final PetSitterCriticalServiceRepository petSitterCriticalServiceRepository;

    @Transactional
    public PetSitterCriticalService registerPetSitterCriticalService(PetSitter petSitter,
            CriticalService criticalService, CriticalServiceRequest criticalServiceRequest) {
        PetSitterCriticalService petSitterCriticalService = PetSitterCriticalService.of(petSitter,
                criticalService, criticalServiceRequest);
        return petSitterCriticalServiceRepository.save(petSitterCriticalService);
    }
}
