package com.ajou_nice.with_pet.dog.service;

import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.critical_service.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.dog.model.dto.DogInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogListInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogSimpleInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogSocializationRequest;
import com.ajou_nice.with_pet.dog.model.dto.add.DogRegisterRequest;
import com.ajou_nice.with_pet.dog.model.dto.add.DogRegisterResponse;
import com.ajou_nice.with_pet.dog.model.dto.delete.DogDeleteResponse;
import com.ajou_nice.with_pet.dog.model.dto.get.DogGetInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.get.DogGetInfosResponse;
import com.ajou_nice.with_pet.dog.model.dto.update.DogUpdateInfoRequest;
import com.ajou_nice.with_pet.dog.model.dto.update.DogUpdateInfoResponse;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.dog.repository.DogRepository;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.group.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogService {
    private final Integer PARTY_MAX_DOG_COUNT = 3;

    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final UserPartyRepository userPartyRepository;
    private final ReservationRepository reservationRepository;
    private final PartyRepository partyRepository;
    private final PetSitterCriticalServiceRepository criticalServiceRepository;

    private final ValidateCollection valid;

    @Transactional
    public DogRegisterResponse registerDog(String email, Long partyId, DogRegisterRequest dogRegisterRequest) {
        User user = userValidationByEmail(email);
        Party party = partyValidationById(partyId);

        Dog dog = authorizationCheckAndRegisterDog(user, party, dogRegisterRequest);
        return DogRegisterResponse.of(dog);
    }

    public DogGetInfoResponse getDogInfo(String email, Long dogId) {
        User user = userValidationByEmail(email);
        Dog dog = dogValidationById(dogId);
        checkPartyAuthorization(user, dog);
        return DogGetInfoResponse.of(dog);
    }

    @Transactional
    public DogUpdateInfoResponse updateDog(String email, Long dogId, DogUpdateInfoRequest dogUpdateInfoRequest) {
        User user = userValidationByEmail(email);
        Dog dog = dogValidationById(dogId);
        checkPartyAuthorization(user, dog);
        updateDog(dog, dogUpdateInfoRequest);
        return DogUpdateInfoResponse.of(dog);
    }

    public List<DogGetInfosResponse> getDogInfos(String email) {
        List<Long> userPartyList = userPartyRepository.findAllUserPartyIdByUserId(email);
        List<Dog> dogs = dogRepository.findAllUserDogs(userPartyList);
        return dogs.stream().map(DogGetInfosResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public DogDeleteResponse deleteDog(String email, Long dogId) {
        User user = userValidationByEmail(email);
        Dog dog = dogValidationById(dogId);
        Party party = dog.getParty();

        return DogDeleteResponse.of(deleteDog(user, dog, party));
    }

    // 캘린더 API에서 사용하는 Dog service (이동 예정)
    public List<DogSimpleInfoResponse> getDogSimpleInfos(String userId) {

        valid.userValidationById(userId);
        List<Dog> dogs = dogRepository.findAllByUserParty(userId);
        return dogs.stream().map(DogSimpleInfoResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public DogInfoResponse modifyDogSocialization(String userId, Long dogId,
                                                  DogSocializationRequest dogSocializationRequest) {
        //유저 체크
        User user = valid.userValidationById(userId);
        //반려견 체크
        Dog dog = valid.dogValidation(dogId);
        //그룹 체크
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "반려견 그룹에 속한 그룹원이 아닙니다.");
        }

        //사회성 정도
        int dogSocialization =
                (int) ((dogSocializationRequest.getQ1() + dogSocializationRequest.getQ2()
                        + dogSocializationRequest.getQ3() +
                        dogSocializationRequest.getQ4() + dogSocializationRequest.getQ5()) / 5)
                        * 20;

        dog.updateSocialization(dogSocialization);
        return DogInfoResponse.of(dog);
    }

    public List<DogListInfoResponse> getDogListInfoResponse(String userId, Long petSitterId) {

        List<Dog> dogs = dogRepository.findAllByUserParty(userId);
        List<PetSitterCriticalService> criticalServices = criticalServiceRepository.findAllByPetSitterId(
                petSitterId);
        List<DogListInfoResponse> dogInfoResponses = new ArrayList<>();
        boolean check;
        for (Dog dog : dogs) {
            check = false;
            for (PetSitterCriticalService criticalService : criticalServices) {
                log.info(
                        "================Dog Size : {}, Critical Size : {} ================================",
                        dog.getDogSize().toString(),
                        criticalService.getCriticalService().getServiceName());
                if (dog.getDogSize().toString()
                        .equals(criticalService.getCriticalService().getServiceName())) {
                    check = true;
                }
            }
            dogInfoResponses.add(DogListInfoResponse.of(dog, check));
        }
        return dogInfoResponses;
    }

    private User userValidationByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
    }

    private Party partyValidationById(Long partyId) {
        return partyRepository.findById(partyId).orElseThrow(() -> {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, ErrorCode.GROUP_NOT_FOUND.getMessage());
        });
    }

    public Dog dogValidationById(Long dogId) {
        return dogRepository.findById(dogId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
    }

    private DogSize getDogSize(Float weight) {
        DogSize dogSize;
        if (weight > 18) {
            dogSize = DogSize.대형견;
        } else if (weight > 10) {
            dogSize = DogSize.중형견;
        } else {
            dogSize = DogSize.소형견;
        }
        return dogSize;
    }

    private Dog authorizationCheckAndRegisterDog(User user, Party party, DogRegisterRequest dogRegisterRequest) {
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, party)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "해당 그룹에 반려견을 추가할 권한이 없습니다.");
        }

        if (party.getDogCount() >= PARTY_MAX_DOG_COUNT) {
            throw new AppException(ErrorCode.TOO_MANY_DOG, ErrorCode.TOO_MANY_DOG.getMessage());
        }

        DogSize myDogSize = getDogSize(dogRegisterRequest.getDogWeight());
        Dog dog = dogRepository.save(Dog.of(dogRegisterRequest, party, myDogSize));
        party.updateDogCount(party.getDogCount() + 1);

        return dog;
    }

    private void checkPartyAuthorization(User user, Dog dog) {
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "반려견 그룹에 속한 그룹원이 아닙니다.");
        }
    }

    private void updateDog(Dog dog, DogUpdateInfoRequest dogUpdateInfoRequest) {
        DogSize myDogSize = getDogSize(dogUpdateInfoRequest.getDogWeight());
        dog.update(dogUpdateInfoRequest, myDogSize);
    }

    private Boolean deleteDog(User user, Dog dog, Party party) {
        Boolean isDeletedParty = Boolean.FALSE;

        checkDogDeleteValidation(user, dog, party);

        dogRepository.delete(dog);
        party.updateDogCount(party.getDogCount() - 1);

        //만약에 멤버가 존재했을 때 실행되는지 테스트 해봐야 함
        if (party.getDogCount() == 0) {
            isDeletedParty = true;
            partyRepository.delete(party);
        }

        return isDeletedParty;
    }

    private void checkDogDeleteValidation(User user, Dog dog, Party party){
        if (!user.getId().equals(party.getPartyLeader().getId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION,
                    ErrorCode.INVALID_PERMISSION.getMessage());
        }

        if (reservationRepository.existsByDogAndReservationStatusIn(dog, Arrays.asList(ReservationStatus.values()))) {
            throw new AppException(ErrorCode.CAN_NOT_DELETE_DOG,
                    ErrorCode.CAN_NOT_DELETE_DOG.getMessage());
        }
    }
}
