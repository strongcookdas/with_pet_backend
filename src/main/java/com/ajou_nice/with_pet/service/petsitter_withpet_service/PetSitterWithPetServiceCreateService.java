package com.ajou_nice.with_pet.service.petsitter_withpet_service;

import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.dto.petsitter_withpet_service.PetSitterWithPetServiceCreateResponse;
import com.ajou_nice.with_pet.dto.petsitter_withpet_service.PetSitterWithPetServiceResponse;
import com.ajou_nice.with_pet.dto.service.ServiceRequest;
import com.ajou_nice.with_pet.service.withpet_service.WithPetServiceService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetSitterWithPetServiceCreateService {

    private final PetSitterWithPetServiceService petSitterWithPetServiceService;
    private final WithPetServiceService withPetServiceService;

    @Transactional
    public List<PetSitterWithPetServiceCreateResponse> registerPetSitterWithPetService(
            PetSitter petSitter, List<ServiceRequest> serviceRequests) {

        List<PetSitterWithPetServiceResponse> petSitterWithPetServiceResponses = new ArrayList<>();

        for (ServiceRequest serviceRequest : serviceRequests) {
            WithPetService withPetService = withPetServiceService.findWithPetService(
                    serviceRequest.getServiceId());
            PetSitterWithPetServiceResponse petSitterWithPetServiceResponse = petSitterWithPetServiceService.resisterPetSitterWithPetService(
                    petSitter, withPetService, serviceRequest);
            petSitterWithPetServiceResponses.add(petSitterWithPetServiceResponse);
        }

        List<WithPetServiceResponse> withPetServiceResponses = withPetServiceService.getWithPetServices();
        List<PetSitterWithPetServiceCreateResponse> petSitterWithPetServiceCreateResponses = new ArrayList<>();
        for (WithPetServiceResponse withPetServiceResponse : withPetServiceResponses) {
            for (PetSitterWithPetServiceResponse petSitterWithPetServiceResponse : petSitterWithPetServiceResponses) {
                if (withPetServiceResponse.getServiceId()
                        == petSitterWithPetServiceResponse.getServiceId()) {
                    petSitterWithPetServiceCreateResponses.add(
                            PetSitterWithPetServiceCreateResponse.of(
                                    petSitterWithPetServiceResponse));
                    break;
                }
                petSitterWithPetServiceCreateResponses.add(
                        PetSitterWithPetServiceCreateResponse.of(withPetServiceResponse));
            }
        }

        return petSitterWithPetServiceCreateResponses;
    }
}
