package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.dog.DogInfoRequest;
import com.ajou_nice.with_pet.domain.dto.dog.DogInfoResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogListInfoResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogSimpleInfoResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogSocializationRequest;
import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;

    private final PetSitterCriticalServiceRepository criticalServiceRepository;


    @Transactional
    public DogInfoResponse registerDog(DogInfoRequest dogInfoRequest, String userId) {
        // 유저 존재 체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        // 파티 생성
        Party party = partyRepository.save(new Party(user));
        party.updateParty(party.getPartyId().toString(), party.getPartyId().toString());

        //반려견 사이즈 체크
        DogSize myDogSize;
        if(dogInfoRequest.getDog_weight() > 18){
            myDogSize = DogSize.대형견;
        }else if(dogInfoRequest.getDog_weight() > 10){
            myDogSize = DogSize.중형견;
        }else{
            myDogSize = DogSize.소형견;
        }

        // 반려견 추가
        Dog dog = dogRepository.save(Dog.of(dogInfoRequest, party, myDogSize));
        userPartyRepository.save(UserParty.of(user, party));

        return DogInfoResponse.of(dog);
    }

    public DogInfoResponse getDogInfo(Long dogId, String userId) {
        //유저 체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //반려견 체크
        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        //그룹 체크
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "반려견 그룹에 속한 그룹원이 아닙니다.");
        }

        return DogInfoResponse.of(dog);
    }

    @Transactional
    public DogInfoResponse modifyDogInfo(Long dogId, DogInfoRequest dogInfoRequest, String userId) {

        //유저 체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //반려견 체크
        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        //그룹 체크
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "반려견 그룹에 속한 그룹원이 아닙니다.");
        }

        DogSize myDogSize;
        if(dogInfoRequest.getDog_weight() > 18){
            myDogSize = DogSize.대형견;
        }else if(dogInfoRequest.getDog_weight() > 10){
            myDogSize = DogSize.중형견;
        }else{
            myDogSize = DogSize.소형견;
        }

        dog.update(dogInfoRequest, myDogSize);

        return DogInfoResponse.of(dog);
    }

    //여기가 제일 문제다....
    public Page<DogInfoResponse> getDogInfos(Pageable pageable, String userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        Page<Dog> dogs = dogRepository.findAllByUserParty(pageable, userId);
        return dogs.map(DogInfoResponse::of);
    }

    public List<DogSimpleInfoResponse> getDogSimpleInfos(String userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        List<Dog> dogs = dogRepository.findAllByUserParty(userId);
        return dogs.stream().map(DogSimpleInfoResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public DogInfoResponse modifyDogSocialization(String userId, Long dogId, DogSocializationRequest dogSocializationRequest){
        //유저 체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //반려견 체크
        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        //그룹 체크
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "반려견 그룹에 속한 그룹원이 아닙니다.");
        }

        //사회성 정도
        int dogSocialization = (int)((dogSocializationRequest.getQ1() + dogSocializationRequest.getQ2()+ dogSocializationRequest.getQ3()+
                dogSocializationRequest.getQ4() + dogSocializationRequest.getQ5())/5)*20;

        dog.updateSocialization(dogSocialization);
        return DogInfoResponse.of(dog);
    }

    public List<DogListInfoResponse> getDogListInfoResponse(String userId, Long petSitterId) {

        List<Dog> dogs = dogRepository.findAllByUserParty(userId);
        List<PetSitterCriticalService> criticalServices = criticalServiceRepository.findAllByPetSitterId(petSitterId);
        List<DogListInfoResponse> dogInfoResponses = new ArrayList<>();
        for(Dog dog : dogs){
            for(PetSitterCriticalService criticalService : criticalServices){
                if(dog.getDogSize().toString().equals(criticalService.getCriticalService().getServiceName())){
                    dogInfoResponses.add(DogListInfoResponse.of(dog,true));

                }
            }
        }
        return null;
    }
}
