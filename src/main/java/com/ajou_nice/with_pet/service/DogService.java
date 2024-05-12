package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.dog.DogInfoRequest;
import com.ajou_nice.with_pet.domain.dto.dog.DogInfoResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogListInfoResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogSimpleInfoResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogSocializationRequest;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PartyRepository;
import com.ajou_nice.with_pet.critical_service.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogService {

    private final Integer dogCount = 3;
    private final DogRepository dogRepository;
    private final UserPartyRepository userPartyRepository;
    private final ReservationRepository reservationRepository;
    private final PartyRepository partyRepository;

    private final PetSitterCriticalServiceRepository criticalServiceRepository;
    private final ValidateCollection valid;


    @Transactional
    public DogInfoResponse registerDog(DogInfoRequest dogInfoRequest, Long partyId,
            String userId) {

        // 유저 존재 체크
        User user = valid.userValidationById(userId);
        // 파티 존재 체크
        Party party = valid.partyValidation(partyId);

        if (!userPartyRepository.existsUserPartyByUserAndParty(user, party)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "해당 그룹에 반려견을 추가할 권한이 없습니다.");
        }

        if (party.getDogCount() >= dogCount) {
            throw new AppException(ErrorCode.TOO_MANY_DOG, ErrorCode.TOO_MANY_DOG.getMessage());
        }
        //반려견 사이즈 체크
        DogSize myDogSize;
        if (dogInfoRequest.getDog_weight() > 18) {
            myDogSize = DogSize.대형견;
        } else if (dogInfoRequest.getDog_weight() > 10) {
            myDogSize = DogSize.중형견;
        } else {
            myDogSize = DogSize.소형견;
        }
        // 반려견 추가
        Dog dog = dogRepository.save(Dog.of(dogInfoRequest, party, myDogSize));
        party.updateDogCount(party.getDogCount() + 1);

        return DogInfoResponse.of(dog);
    }

    public DogInfoResponse getDogInfo(Long dogId, String userId) {
        //유저 체크
        User user = valid.userValidationById(userId);

        //반려견 체크
        Dog dog = valid.dogValidation(dogId);
        //그룹 체크
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "반려견 그룹에 속한 그룹원이 아닙니다.");
        }

        return DogInfoResponse.of(dog);
    }

    @Transactional
    public DogInfoResponse modifyDogInfo(Long dogId, DogInfoRequest dogInfoRequest, String userId) {

        //유저 체크
        User user = valid.userValidationById(userId);
        //반려견 체크
        Dog dog = valid.dogValidation(dogId);
        //그룹 체크
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "반려견 그룹에 속한 그룹원이 아닙니다.");
        }

        DogSize myDogSize;
        if (dogInfoRequest.getDog_weight() > 18) {
            myDogSize = DogSize.대형견;
        } else if (dogInfoRequest.getDog_weight() > 10) {
            myDogSize = DogSize.중형견;
        } else {
            myDogSize = DogSize.소형견;
        }

        dog.update(dogInfoRequest, myDogSize);

        return DogInfoResponse.of(dog);
    }

    //여기가 제일 문제다....
    public List<DogInfoResponse> getDogInfos(String userId) {
        List<Long> userPartyList = userPartyRepository.findAllUserPartyIdByUserId(userId);
        log.info(
                "==================================== findAllUserDogs Start ==============================================");
        List<Dog> dogs = dogRepository.findAllUserDogs(userPartyList);
        log.info(
                "==================================== findAllUserDogs End ==============================================");
        return dogs.stream().map(DogInfoResponse::of).collect(Collectors.toList());
    }

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

    @Transactional
    public Boolean deleteDog(String userId, Long dogId) {
        Boolean deleteParty = false;
        User user = valid.userValidationById(userId);

        Dog dog = valid.dogValidation(dogId);

        Party party = dog.getParty();

        if (!user.getId().equals(dog.getParty().getUser().getId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION,
                    ErrorCode.INVALID_PERMISSION.getMessage());
        }

        List<ReservationStatus> reservationStatuses = new ArrayList<>();
        reservationStatuses.add(ReservationStatus.APPROVAL);
        reservationStatuses.add(ReservationStatus.PAYED);
        reservationStatuses.add(ReservationStatus.USE);
        reservationStatuses.add(ReservationStatus.WAIT);

        if (reservationRepository.existsByDogAndReservationStatusIn(dog, reservationStatuses)) {
            throw new AppException(ErrorCode.CAN_NOT_DELETE_DOG,
                    ErrorCode.CAN_NOT_DELETE_DOG.getMessage());
        }

        dogRepository.delete(dog);
        party.updateDogCount(party.getDogCount() - 1);

        if (party.getDogCount() == 0) {
            deleteParty = true;
            partyRepository.delete(party);
        }

        return deleteParty;
    }
}
