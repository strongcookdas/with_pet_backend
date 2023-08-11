package com.ajou_nice.with_pet.service.petsitter_withpet_service;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.dto.petsitter_withpet_service.PetSitterWithPetServiceResponse;
import com.ajou_nice.with_pet.dto.service.ServiceRequest;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PetSitterWithPetServiceRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetSitterWithPetServiceService {

    private final PetSitterWithPetServiceRepository petSitterWithPetServiceRepository;

    /**
     * 펫시터 부가 서비스 등록
     **/
    @Transactional
    public PetSitterWithPetServiceResponse resisterPetSitterWithPetService(
            PetSitter petSitter, WithPetService withPetService,
            ServiceRequest serviceRequest) {

        PetSitterWithPetService petSitterWithPetService = petSitterWithPetServiceRepository.save(
                PetSitterWithPetService.of(petSitter, withPetService, serviceRequest.getPrice()));

        return PetSitterWithPetServiceResponse.of(petSitterWithPetService);
    }

}
