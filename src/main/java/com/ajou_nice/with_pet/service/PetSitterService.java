package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse.PetSitterModifyInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterMainResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterCriticalServiceRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterCriticalServicesRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHashTagRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHashTagsRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHouseRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHousesRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterInfoRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterIntroRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterServiceRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterWithPetServicesRequest;
import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.House;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitterHashTag;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.Review;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.critical_service.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.repository.HouseRepository;
import com.ajou_nice.with_pet.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.repository.PetSitterHashTagRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.PetSitterServiceRepository;
import com.ajou_nice.with_pet.repository.ReviewRepository;
import com.ajou_nice.with_pet.withpet_service.repository.WithPetServiceRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


	// == 펫시터 상세정보 조회 == //
	public PetSitterDetailInfoResponse showPetSitterDetailInfo(Long petSitterId){

		PetSitter findPetSitter = valid.petSitterValidation(petSitterId);
		List<Review> reviews = reviewRepository.findAllByPetSitter(findPetSitter);
		List<PetSitterWithPetService> petSitterWithPetServices = petSitterServiceRepository.findAllByPetSitterInQuery(
				findPetSitter.getId());

		List<PetSitterCriticalService> petSitterCriticalServices = petSitterCriticalServiceRepository.findAllByPetSitterInQuery(
				findPetSitter.getId());

		return PetSitterDetailInfoResponse.of(findPetSitter, reviews, petSitterWithPetServices, petSitterCriticalServices);
	}

	// == 펫시터의 자신 정보 조회 == //
	public PetSitterModifyInfoResponse showMyInfo(String email){

		User findUser = valid.userValidationByEmail(email);

		PetSitter petSitter = valid.petSitterValidationByUser(findUser);

		List<WithPetService> withPetServiceList = getWithPetServiceList();
		List<CriticalService> criticalServiceList = getCriticalServiceList();
		List<PetSitterWithPetService> petSitterWithPetServices = getPetSitterWithPetServiceList(petSitter);
		List<PetSitterCriticalService> petSitterCriticalServices = getPetSitterCriticalServiceList(petSitter);

		return PetSitterModifyInfoResponse.of(petSitter, criticalServiceList, withPetServiceList, petSitterWithPetServices, petSitterCriticalServices);
	}

	// == 펫시터 my Info 등록 == //
	@Transactional
	public PetSitterModifyInfoResponse registerPetSitterInfo(
			PetSitterInfoRequest petSitterInfoRequest, String email){

		User findUser = valid.userValidationByEmail(email);

		PetSitter petSitter = valid.petSitterValidationByUser(findUser);

		Iterator<PetSitterHouseRequest> petSitterHouses = petSitterInfoRequest.getPetSitterHouseRequests().iterator();
		List<House> houses = addPetSitterHouseInfos(petSitterHouses, petSitter);

		Iterator<PetSitterHashTagRequest> petSitterHashtags = petSitterInfoRequest.getPetSitterHashTagRequests().iterator();
		List<PetSitterHashTag> hashTags = addPetSitterHashTagInfos(petSitterHashtags, petSitter);

		Iterator<PetSitterServiceRequest> petSitterServices = petSitterInfoRequest.getPetSitterServiceRequests().iterator();
		List<PetSitterWithPetService> services = addPetSitterWithPetServiceInfos(petSitterServices, petSitter);

		Iterator<PetSitterCriticalServiceRequest> petSitterCriticalServiceRequests = petSitterInfoRequest.getPetSitterCriticalServiceRequests().iterator();
		List<PetSitterCriticalService> criticalServices = addPetSitterCriticalServiceInfos(petSitterCriticalServiceRequests, petSitter);

		houseRepository.saveAll(houses);
		petSitterHashTagRepository.saveAll(hashTags);
		petSitterServiceRepository.saveAll(services);
		petSitterCriticalServiceRepository.saveAll(criticalServices);
		petSitter.updateIntroduction(petSitterInfoRequest.getIntroduction());
		// 펫시터 정보를 입력한 다음 validation이 true로 바뀌어서 Main page에 조회가 된다.
		petSitter.changeValidation(true);

		List<WithPetService> withPetServiceList = getWithPetServiceList();
		List<CriticalService> criticalServiceList = getCriticalServiceList();

		return PetSitterModifyInfoResponse.of(petSitter, criticalServiceList, withPetServiceList, services, criticalServices);
	}

	// == 펫시터 houses 정보 수정 == //
	@Transactional
	public void updateHouseInfo(PetSitterHousesRequest petSitterHousesRequest, String userId){

		User findUser = valid.userValidation(userId);

		PetSitter petSitter = valid.petSitterValidationByUser(findUser);

		//원래 있던 정보 전체 삭제
		List<House> petSitterHouseList = houseRepository.findAllByPetSitterInQuery(petSitter.getId());

		if(!petSitterHouseList.isEmpty()){
			houseRepository.deleteAllByPetSitterInQuery(petSitter.getId());
		}
		//새로운 정보로 갈아 끼움 houses
		Iterator<PetSitterHouseRequest> petSitterHouses = petSitterHousesRequest.getPetSitterHousesRequests().iterator();
		List<House> newHouseList = addPetSitterHouseInfos(petSitterHouses, petSitter);

		houseRepository.saveAll(newHouseList);
	}

	// == 펫시터 HashTags 정보 수정 == //
	@Transactional
	public void updateHashTagInfo(PetSitterHashTagsRequest hashTagsRequest, String userId){

		User findUser = valid.userValidation(userId);

		PetSitter petSitter = valid.petSitterValidationByUser(findUser);

		List<PetSitterHashTag> petSitterHashTagList = petSitterHashTagRepository.findAllByPetSitterInQuery(petSitter.getId());
		if(!petSitterHashTagList.isEmpty()){
			petSitterHashTagRepository.deleteAllByPetSitterInQuery(petSitter.getId());
		}

		//새로운 정보로 갈아 끼움 hashTags
		Iterator<PetSitterHashTagRequest> petSitterHashtags = hashTagsRequest.getPetSitterHashTagRequests().iterator();
		List<PetSitterHashTag> newHashTagList = addPetSitterHashTagInfos(petSitterHashtags, petSitter);


		petSitterHashTagRepository.saveAll(newHashTagList);
	}

	// == 펫시터 WithPetService 정보 수정 == //
	@Transactional
	public void updatePetSitterService(PetSitterWithPetServicesRequest withPetServicesRequest, String userId){
		User findUser = valid.userValidation(userId);

		PetSitter petSitter = valid.petSitterValidationByUser(findUser);


		List<PetSitterWithPetService> petSitterServiceList = petSitterServiceRepository.findAllByPetSitterInQuery(petSitter.getId());
		if(!petSitterServiceList.isEmpty()){
			petSitterServiceRepository.deleteAllByPetSitterInQuery(petSitter.getId());
		}
		Iterator<PetSitterServiceRequest> petSitterServices = withPetServicesRequest.getPetSitterServiceRequests().iterator();

		//새로운 정보로 갈아 끼움 petSitterservices
		List<PetSitterWithPetService> newPetSitterServices = addPetSitterWithPetServiceInfos(petSitterServices, petSitter);

		petSitterServiceRepository.saveAll(newPetSitterServices);
	}

	// == 펫시터 CriticalPetService 정보 수정 == //
	@Transactional
	public void updateCriticalService(PetSitterCriticalServicesRequest criticalServicesRequest, String userId){
		User findUser = valid.userValidation(userId);

		PetSitter petSitter = valid.petSitterValidationByUser(findUser);

		List<PetSitterCriticalService> petSitterCriticalServiceList = petSitterCriticalServiceRepository.findAllByPetSitterInQuery(petSitter.getId());
		if(!petSitterCriticalServiceList.isEmpty()){
			petSitterCriticalServiceRepository.deleteAllByPetSitterInQuery(petSitter.getId());
		}
		Iterator<PetSitterCriticalServiceRequest> petSitterCriticalServices = criticalServicesRequest.getPetSitterCriticalServiceRequests().iterator();

		//새로운 정보로 갈아 끼움 criticalServices
		List<PetSitterCriticalService> newCriticalServices = addPetSitterCriticalServiceInfos(petSitterCriticalServices, petSitter);

		petSitterCriticalServiceRepository.saveAll(newCriticalServices);
	}

	// == 펫시터 introduction 수정 == //
	@Transactional
	public void updatePetSitterIntro(PetSitterIntroRequest petSitterIntroRequest, String userId){
		User findUser = valid.userValidation(userId);

		PetSitter petSitter = valid.petSitterValidationByUser(findUser);

		petSitter.updateIntroduction(petSitterIntroRequest.getIntroduction());
	}

	// == 메인페이지 펫시터들 정보 조회 == //
	// 리팩토링 무조건 무조건 필요 .. 쿼리가 너무 많이 나간다.
	// userName을 불러오기 위해서 user테이블에서 조회하는 쿼리가 생기는데 이를 해결해야 한다..
	public Page getPetSitters(Pageable pageable, String dogSize, List<String> service, String address){
		Page<PetSitter> petSitters = petSitterRepository.searchPage(pageable, dogSize, service, address);
		return petSitters.map(PetSitterMainResponse::of);
	}

	// 위드펫 서비스 리스트 return
	private List<WithPetService> getWithPetServiceList(){
		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();
		return withPetServiceList;
	}

	// 필수 위드펫 서비스 리스트 return
	private List<CriticalService> getCriticalServiceList(){
		List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();
		return criticalServiceList;
	}

	// 펫시터 위드펫 서비스 리스트 return
	private List<PetSitterWithPetService> getPetSitterWithPetServiceList(PetSitter petSitter){
		List<PetSitterWithPetService> petSitterWithPetServices = petSitterServiceRepository.findAllByPetSitterInQuery(
				petSitter.getId());
		return petSitterWithPetServices;
	}

	//펫시터 필수 서비스 리스트 return
	private List<PetSitterCriticalService> getPetSitterCriticalServiceList(PetSitter petSitter){
		List<PetSitterCriticalService> petSitterCriticalServices = petSitterCriticalServiceRepository.findAllByPetSitterInQuery(
				petSitter.getId());
		return petSitterCriticalServices;
	}

	// 펫시터 집 정보 update
	private List<House> addPetSitterHouseInfos(Iterator<PetSitterHouseRequest> petSitterHouses, PetSitter petSitter){

		List<House> houses = new ArrayList<>();
		while(petSitterHouses.hasNext()){
			PetSitterHouseRequest houseRequest = petSitterHouses.next();
			House house = House.toEntity(petSitter, houseRequest);
			houses.add(house);
		}
		return houses;
	}

	// 펫시터 hashTag 정보 update
	private List<PetSitterHashTag> addPetSitterHashTagInfos(Iterator<PetSitterHashTagRequest> petSitterHashTags, PetSitter petSitter){

		List<PetSitterHashTag> hashTags = new ArrayList<>();
		while(petSitterHashTags.hasNext()){
			PetSitterHashTagRequest hashTagRequest = petSitterHashTags.next();
			PetSitterHashTag petSitterHashTag = PetSitterHashTag.toEntity(petSitter, hashTagRequest);
			hashTags.add(petSitterHashTag);
		}
		return hashTags;
	}

	// 펫시터 위드펫 서비스 정보 update
	private List<PetSitterWithPetService> addPetSitterWithPetServiceInfos(Iterator<PetSitterServiceRequest> petSitterServices, PetSitter petSitter){

		List<PetSitterWithPetService> services = new ArrayList<>();

		while(petSitterServices.hasNext()){
			PetSitterServiceRequest serviceRequest = petSitterServices.next();
			WithPetService withPetService = valid.withPetServiceValidation(
					serviceRequest.getServiceId());
			PetSitterWithPetService petSitterWithPetService = PetSitterWithPetService.toEntity(withPetService,petSitter,serviceRequest.getPrice());
			services.add(petSitterWithPetService);
		}
		return services;
	}

	// 펫시터 필수 서비스 정보 update
	private List<PetSitterCriticalService> addPetSitterCriticalServiceInfos(Iterator<PetSitterCriticalServiceRequest> petSitterCriticalServiceRequests, PetSitter petSitter){
		List<PetSitterCriticalService> criticalServices = new ArrayList<>();

		while(petSitterCriticalServiceRequests.hasNext()){
			PetSitterCriticalServiceRequest criticalServiceRequest = petSitterCriticalServiceRequests.next();
			CriticalService criticalService = valid.criticalServiceValidation(
					criticalServiceRequest.getServiceId());
			if(criticalServiceRequest.getServiceId() == 1){
				petSitter.changeAvailableDogSize(DogSize.소형견);
			}else if(criticalServiceRequest.getServiceId() == 2){
				petSitter.changeAvailableDogSize(DogSize.중형견);
			}else{
				petSitter.changeAvailableDogSize(DogSize.대형견);
			}
			PetSitterCriticalService petSitterCriticalService = PetSitterCriticalService.toEntity(criticalService, petSitter,criticalServiceRequest.getPrice());
			criticalServices.add(petSitterCriticalService);
		}
		return criticalServices;
	}
}
