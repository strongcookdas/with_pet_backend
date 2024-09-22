package com.ajou_nice.with_pet.dog.service;

import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.dog.repository.DogRepository;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DogValidationService {
    private final DogRepository dogRepository;

    public Dog dogValidationById(Long dogId) {
        return dogRepository.findById(dogId).orElseThrow(() -> new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage()));
    }
}
