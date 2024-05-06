package com.ajou_nice.with_pet.withpet_service.service;

import com.ajou_nice.with_pet.withpet_service.model.dto.WithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import com.ajou_nice.with_pet.withpet_service.repository.WithPetServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WithPetServiceService {
    private final WithPetServiceRepository withPetServiceRepository;
    public List<WithPetServiceResponse> showWithPetServices() {
        List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();
        return WithPetServiceResponse.toList(withPetServiceList);
    }
}
