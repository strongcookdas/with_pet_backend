package com.ajou_nice.with_pet.service.withpet_service;

import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.WithPetServiceRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithPetServiceService {

    private final WithPetServiceRepository withPetServiceRepository;

    public WithPetService findWithPetService(Long serviceId) {
        return withPetServiceRepository.findById(serviceId).orElseThrow(() -> {
            throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND,
                    ErrorCode.WITH_PET_SERVICE_NOT_FOUND.name());
        });
    }

    public List<WithPetService> getWithPetServices() {
        return withPetServiceRepository.findAll();
    }
}
