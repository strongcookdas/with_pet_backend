package com.ajou_nice.with_pet.petsitter.service;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.petsitter.repository.PetSitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetSitterValidationService {
    private final PetSitterRepository petSitterRepository;

    public PetSitter petSitterValidationByUser(User user) {
        return petSitterRepository.findByUser(user).orElseThrow(() -> new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage()));
    }

    public PetSitter petSitterValidationByPetSitterId(Long petSitterId) {
        return petSitterRepository.findById(petSitterId).orElseThrow(() -> new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage()));
    }
}
