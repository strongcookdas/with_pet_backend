package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.dog.DogInfoRequest;
import com.ajou_nice.with_pet.domain.dto.dog.DogInfoResponse;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;

    public DogInfoResponse registerDog(DogInfoRequest dogInfoRequest) {
        Dog dog = Dog.of(dogInfoRequest);
        Dog saveDog = dogRepository.save(dog);
        return DogInfoResponse.of(saveDog);
    }

    public DogInfoResponse getDogInfo(Long dogId) {
        Dog findDog = dogRepository.findById(dogId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });

        return DogInfoResponse.of(findDog);
    }

    @Transactional
    public DogInfoResponse modifyDogInfo(Long dogId, DogInfoRequest dogInfoRequest) {
        Dog findDog = dogRepository.findById(dogId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });

        findDog.update(dogInfoRequest);

        return DogInfoResponse.of(findDog);
    }

    public List<DogInfoResponse> getDogInfos() {

        List<DogInfoResponse> dogInfoResponses = DogInfoResponse.toList(dogRepository.findAll());
        return dogInfoResponses;
    }
}
