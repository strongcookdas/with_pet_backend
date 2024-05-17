package com.ajou_nice.with_pet.petsitter.service;

import com.ajou_nice.with_pet.critical_service.model.dto.PetSitterAddCriticalServiceRequest;
import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.critical_service.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.critical_service.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.domain.entity.Review;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.hashtag.model.entity.PetSitterHashTag;
import com.ajou_nice.with_pet.hashtag.repository.PetSitterHashTagRepository;
import com.ajou_nice.with_pet.house.model.entity.House;
import com.ajou_nice.with_pet.house.repository.HouseRepository;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterMainResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterRequest.*;
import com.ajou_nice.with_pet.petsitter.model.dto.detail.PetSitterDetailInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.detail.PetSitterDetailInfoResponse.PetSitterMyInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.house.PetSitterUpdateHousesRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.register_info.PetSitterRegisterInfoRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.register_info.PetSitterRegisterInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.update_hash_tag.PetSitterHashTagsRequest;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.petsitter.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.ReviewRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.withpet_service.model.dto.PetSitterAddServiceRequest;
import com.ajou_nice.with_pet.withpet_service.model.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import com.ajou_nice.with_pet.withpet_service.repository.PetSitterServiceRepository;
import com.ajou_nice.with_pet.withpet_service.repository.WithPetServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetSitterService {

    private final PetSitterRepository petSitterRepository;
    private final PetSitterServiceRepository petSitterServiceRepository;
    private final WithPetServiceRepository withPetServiceRepository;
    private final HouseRepository houseRepository;
    private final PetSitterHashTagRepository petSitterHashTagRepository;
    private final CriticalServiceRepository criticalServiceRepository;
    private final PetSitterCriticalServiceRepository petSitterCriticalServiceRepository;
    private final ReviewRepository reviewRepository;
    private final ValidateCollection valid;

    public PetSitterDetailInfoResponse getPetSitterDetailInfo(Long petSitterId) {
        PetSitter findPetSitter = petSitterValidationById(petSitterId);

        List<Review> reviews = reviewRepository.findAllByPetSitter(findPetSitter);
        List<PetSitterWithPetService> petSitterWithPetServices = petSitterServiceRepository.findAllByPetSitterInQuery(findPetSitter.getId());
        List<PetSitterCriticalService> petSitterCriticalServices = petSitterCriticalServiceRepository.findAllByPetSitterInQuery(findPetSitter.getId());

        return PetSitterDetailInfoResponse.of(findPetSitter, reviews, petSitterWithPetServices, petSitterCriticalServices);
    }

    public PetSitterMyInfoResponse getMyInfo(String email) {
        PetSitter petSitter = petSitterValidationByEmail(email);

        List<WithPetService> withPetServiceList = getWithPetServiceList();
        List<CriticalService> criticalServiceList = getCriticalServiceList();
        List<PetSitterWithPetService> petSitterWithPetServices = getPetSitterWithPetServiceList(petSitter);
        List<PetSitterCriticalService> petSitterCriticalServices = getPetSitterCriticalServiceList(petSitter);

        return PetSitterMyInfoResponse.of(petSitter, criticalServiceList, withPetServiceList, petSitterWithPetServices, petSitterCriticalServices);
    }

    // == 펫시터 my Info 등록 == //
    @Transactional
    public PetSitterRegisterInfoResponse registerPetSitterInfo(String email, PetSitterRegisterInfoRequest petSitterRegisterInfoRequest) {

        PetSitter petSitter = petSitterValidationByEmail(email);

        List<House> houses = House.toList(petSitter, petSitterRegisterInfoRequest.getPetSitterHouseRequests());
        List<PetSitterHashTag> hashTags = PetSitterHashTag.toList(petSitter, petSitterRegisterInfoRequest.getPetSitterHashTagRequests());
        List<PetSitterWithPetService> services = addPetSitterWithPetServiceInfos(petSitter, petSitterRegisterInfoRequest.getPetSitterServiceRequests());
        List<PetSitterCriticalService> criticalServices = addPetSitterCriticalServiceInfos(petSitter, petSitterRegisterInfoRequest.getPetSitterCriticalServiceRequests());

        houseRepository.saveAll(houses);
        petSitterHashTagRepository.saveAll(hashTags);
        petSitterServiceRepository.saveAll(services);
        petSitterCriticalServiceRepository.saveAll(criticalServices);
        petSitter.updateIntroduction(petSitterRegisterInfoRequest.getPetSitterIntroduction());

        // 펫시터 정보를 입력한 다음 validation이 true로 바뀌어서 Main page에 조회가 된다.
        petSitter.changeValidation(true);

        List<WithPetService> withPetServiceList = getWithPetServiceList();
        List<CriticalService> criticalServiceList = getCriticalServiceList();

        return PetSitterRegisterInfoResponse.of(petSitter, criticalServiceList, withPetServiceList, services, criticalServices);
    }

    @Transactional
    public void updatePetSitterHouses(String email, PetSitterUpdateHousesRequest petSitterUpdateHousesRequest) {
        PetSitter petSitter = petSitterValidationByEmail(email);

        List<House> petSitterHouseList = houseRepository.findAllByPetSitterInQuery(petSitter.getId());
        if (!petSitterHouseList.isEmpty()) {
            houseRepository.deleteAllByPetSitterInQuery(petSitter.getId());
        }

        List<House> newHouseList = House.updateHouses(petSitter, petSitterUpdateHousesRequest.getPetSitterHousesRequests());
        houseRepository.saveAll(newHouseList);
    }

    @Transactional
    public void updateHashTags(String email, PetSitterHashTagsRequest hashTagsRequest) {
        PetSitter petSitter = petSitterValidationByEmail(email);

        List<PetSitterHashTag> petSitterHashTagList = petSitterHashTagRepository.findAllByPetSitterInQuery(petSitter.getId());
        if (!petSitterHashTagList.isEmpty()) {
            petSitterHashTagRepository.deleteAllByPetSitterInQuery(petSitter.getId());
        }

        List<PetSitterHashTag> newHashTagList = PetSitterHashTag.updateHashTags(petSitter, hashTagsRequest.getPetSitterHashTagRequests());
        petSitterHashTagRepository.saveAll(newHashTagList);
    }

    // == 펫시터 WithPetService 정보 수정 == //
    @Transactional
    public void updatePetSitterService(PetSitterWithPetServicesRequest withPetServicesRequest, String userId) {
        User findUser = valid.userValidationById(userId);

        PetSitter petSitter = valid.petSitterValidationByUser(findUser);


        List<PetSitterWithPetService> petSitterServiceList = petSitterServiceRepository.findAllByPetSitterInQuery(petSitter.getId());
        if (!petSitterServiceList.isEmpty()) {
            petSitterServiceRepository.deleteAllByPetSitterInQuery(petSitter.getId());
        }
        Iterator<PetSitterServiceRequest> petSitterServices = withPetServicesRequest.getPetSitterServiceRequests().iterator();

        //새로운 정보로 갈아 끼움 petSitterservices
//        List<PetSitterWithPetService> newPetSitterServices = addPetSitterWithPetServiceInfos(petSitterServices, petSitter);

//        petSitterServiceRepository.saveAll(newPetSitterServices);
    }

    // == 펫시터 CriticalPetService 정보 수정 == //
    @Transactional
    public void updateCriticalService(PetSitterCriticalServicesRequest criticalServicesRequest, String userId) {
        User findUser = valid.userValidationById(userId);

        PetSitter petSitter = valid.petSitterValidationByUser(findUser);

        List<PetSitterCriticalService> petSitterCriticalServiceList = petSitterCriticalServiceRepository.findAllByPetSitterInQuery(petSitter.getId());
        if (!petSitterCriticalServiceList.isEmpty()) {
            petSitterCriticalServiceRepository.deleteAllByPetSitterInQuery(petSitter.getId());
        }
        Iterator<PetSitterCriticalServiceRequest> petSitterCriticalServices = criticalServicesRequest.getPetSitterCriticalServiceRequests().iterator();

        //새로운 정보로 갈아 끼움 criticalServices
//        List<PetSitterCriticalService> newCriticalServices = addPetSitterCriticalServiceInfos(petSitterCriticalServices, petSitter);

//        petSitterCriticalServiceRepository.saveAll(newCriticalServices);
    }

    // == 펫시터 introduction 수정 == //
    @Transactional
    public void updatePetSitterIntro(PetSitterIntroRequest petSitterIntroRequest, String userId) {
        User findUser = valid.userValidationById(userId);

        PetSitter petSitter = valid.petSitterValidationByUser(findUser);

        petSitter.updateIntroduction(petSitterIntroRequest.getIntroduction());
    }

    // == 메인페이지 펫시터들 정보 조회 == //
    // 리팩토링 무조건 무조건 필요 .. 쿼리가 너무 많이 나간다.
    // userName을 불러오기 위해서 user테이블에서 조회하는 쿼리가 생기는데 이를 해결해야 한다..
    public Page getPetSitters(Pageable pageable, String dogSize, List<String> service, String address) {
        Page<PetSitter> petSitters = petSitterRepository.searchPage(pageable, dogSize, service, address);
        return petSitters.map(PetSitterMainResponse::of);
    }

    private PetSitter petSitterValidationByEmail(String email) {
        PetSitter petSitter = petSitterRepository.findByUserEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });
        return petSitter;
    }

    private PetSitter petSitterValidationById(Long petSitterId) {
        PetSitter findPetSitter = petSitterRepository.findById(petSitterId).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });
        return findPetSitter;
    }

    private WithPetService withPetServiceValidation(Long serviceId) {
        WithPetService withPetService = withPetServiceRepository.findById(serviceId).orElseThrow(() -> {
            throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND, ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
        });
        return withPetService;
    }

    private CriticalService criticalServiceValidation(Long serviceId) {
        CriticalService criticalService = criticalServiceRepository.findById(serviceId).orElseThrow(() -> {
            throw new AppException(ErrorCode.CRITICAL_SERVICE_NOT_FOUND,
                    ErrorCode.CRITICAL_SERVICE_NOT_FOUND.getMessage());
        });
        return criticalService;
    }

    // 위드펫 서비스 리스트 return
    private List<WithPetService> getWithPetServiceList() {
        List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();
        return withPetServiceList;
    }

    // 필수 위드펫 서비스 리스트 return
    private List<CriticalService> getCriticalServiceList() {
        List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();
        return criticalServiceList;
    }

    // 펫시터 위드펫 서비스 리스트 return
    private List<PetSitterWithPetService> getPetSitterWithPetServiceList(PetSitter petSitter) {
        List<PetSitterWithPetService> petSitterWithPetServices = petSitterServiceRepository.findAllByPetSitterInQuery(
                petSitter.getId());
        return petSitterWithPetServices;
    }

    //펫시터 필수 서비스 리스트 return
    private List<PetSitterCriticalService> getPetSitterCriticalServiceList(PetSitter petSitter) {
        List<PetSitterCriticalService> petSitterCriticalServices = petSitterCriticalServiceRepository.findAllByPetSitterInQuery(
                petSitter.getId());
        return petSitterCriticalServices;
    }

    // 펫시터 hashTag 정보 update
    private List<PetSitterHashTag> addPetSitterHashTagInfos(Iterator<PetSitterHashTagRequest> petSitterHashTags, PetSitter petSitter) {

        List<PetSitterHashTag> hashTags = new ArrayList<>();
        while (petSitterHashTags.hasNext()) {
            PetSitterHashTagRequest hashTagRequest = petSitterHashTags.next();
            PetSitterHashTag petSitterHashTag = PetSitterHashTag.toEntity(petSitter, hashTagRequest);
            hashTags.add(petSitterHashTag);
        }
        return hashTags;
    }

    // 펫시터 위드펫 서비스 정보 update
    private List<PetSitterWithPetService> addPetSitterWithPetServiceInfos(PetSitter petSitter, List<PetSitterAddServiceRequest> serviceRequests) {

        List<PetSitterWithPetService> services = new ArrayList<>();

        for (PetSitterAddServiceRequest request : serviceRequests) {
            WithPetService withPetService = withPetServiceValidation(request.getServiceId());
            PetSitterWithPetService petSitterWithPetService = PetSitterWithPetService.toEntity(withPetService, petSitter, request.getPrice());
            services.add(petSitterWithPetService);
        }
        return services;
    }

    // 펫시터 필수 서비스 정보 update
    private List<PetSitterCriticalService> addPetSitterCriticalServiceInfos(PetSitter petSitter, List<PetSitterAddCriticalServiceRequest> criticalServiceRequests) {
        List<PetSitterCriticalService> criticalServices = new ArrayList<>();

        for (PetSitterAddCriticalServiceRequest request : criticalServiceRequests) {
            CriticalService criticalService = criticalServiceValidation(request.getServiceId());

            if (request.getServiceId() == 1) { // 해당 코드의 역할이 파악x
                petSitter.changeAvailableDogSize(DogSize.소형견);
            } else if (request.getServiceId() == 2) {
                petSitter.changeAvailableDogSize(DogSize.중형견);
            } else {
                petSitter.changeAvailableDogSize(DogSize.대형견);
            }

            PetSitterCriticalService petSitterCriticalService = PetSitterCriticalService.toEntity(criticalService, petSitter, request.getPrice());
            criticalServices.add(petSitterCriticalService);
        }
        return criticalServices;
    }
}
