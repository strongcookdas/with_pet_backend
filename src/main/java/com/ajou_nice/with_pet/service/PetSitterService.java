package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse.PetSitterModifyInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterMainResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterCriticalServiceRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHashTagRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHouseRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterModifyInfoRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterServiceRequest;
import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.House;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitterHashTag;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.repository.HouseRepository;
import com.ajou_nice.with_pet.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.repository.PetSitterHashTagRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.PetSitterServiceRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.repository.WithPetServiceRepository;
import com.ajou_nice.with_pet.service.user.UserService;
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
	private final UserRepository userRepository;
	private final HouseRepository houseRepository;
	private final PetSitterHashTagRepository petSitterHashTagRepository;

	private final CriticalServiceRepository criticalServiceRepository;

	private final PetSitterCriticalServiceRepository petSitterCriticalServiceRepository;
	private final UserService userService;


	// == 펫시터 상세정보 조회 == //
	public PetSitterDetailInfoResponse showPetSitterDetailInfo(Long petSitterId){
		PetSitter findPetSitter = petSitterRepository.findById(petSitterId).orElseThrow(()->{
			throw new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage());
		});

		return PetSitterDetailInfoResponse.of(findPetSitter);
	}

	// == 펫시터의 자신 정보 조회 == //
	public PetSitterModifyInfoResponse showMyInfo(String userId){
		User findUser = userService.findUser(userId);

		PetSitter petSitter = petSitterRepository.findByUser(findUser).orElseThrow(()->{
			throw new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage());
		});

		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();
		List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();

		return PetSitterModifyInfoResponse.of(petSitter, criticalServiceList, withPetServiceList);
	}

	// == 펫시터 my Info 수정 == //
	// 로직 필요한 것 : petsitter의 수정요청이 들어오면 선택한 criticalservice로 가능한 dogsize를 업데이트 해줘야하는데 어떻게 하지 ?
	@Transactional
	public PetSitterModifyInfoResponse updateMyInfo(PetSitterModifyInfoRequest petSitterModifyInfoRequest, String userId){
		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		PetSitter petSitter = petSitterRepository.findByUser(findUser).orElseThrow(()->{
			throw new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage());
		});

		//원래 있던 정보 전체 삭제
		List<House> petSitterHouseList = houseRepository.findAllByPetSitterInQuery(petSitter.getId());

		if(!petSitterHouseList.isEmpty()){
			houseRepository.deleteAllByPetSitterInQuery(petSitter.getId());
		}

		List<PetSitterWithPetService> petSitterServiceList = petSitterServiceRepository.findAllByPetSitterInQuery(petSitter.getId());
		if(!petSitterServiceList.isEmpty()){
			petSitterServiceRepository.deleteAllByPetSitterInQuery(petSitter.getId());
		}

		List<PetSitterHashTag> petSitterHashTagList = petSitterHashTagRepository.findAllByPetSitterInQuery(petSitter.getId());
		if(!petSitterHashTagList.isEmpty()){
			petSitterHashTagRepository.deleteAllByPetSitterInQuery(petSitter.getId());
		}

		List<PetSitterCriticalService> petSitterCriticalServiceList = petSitterCriticalServiceRepository.findAllByPetSitterInQuery(petSitter.getId());
		if(!petSitterCriticalServiceList.isEmpty()){
			petSitterCriticalServiceRepository.deleteAllByPetSitterInQuery(petSitter.getId());
		}

		//새로운 정보로 갈아 끼움 (houses, hashtags, petsitterservices, introduction)
		Iterator<PetSitterHouseRequest> petSitterHouses = petSitterModifyInfoRequest.getPetSitterHouseRequests().iterator();
		while(petSitterHouses.hasNext()){
			PetSitterHouseRequest houseRequest = petSitterHouses.next();
			House house = House.toEntity(petSitter, houseRequest);
			houseRepository.save(house);
		}
		Iterator<PetSitterHashTagRequest> petSitterHashtags = petSitterModifyInfoRequest.getPetSitterHashTagRequests().iterator();
		while(petSitterHashtags.hasNext()){
			PetSitterHashTagRequest hashTagRequest = petSitterHashtags.next();
			PetSitterHashTag petSitterHashTag = PetSitterHashTag.toEntity(petSitter, hashTagRequest);
			petSitterHashTagRepository.save(petSitterHashTag);
		}


		// 리팩토링 필요하다. (어떻게 ?)
		Iterator<PetSitterServiceRequest> petSitterServices = petSitterModifyInfoRequest.getPetSitterServiceRequests().iterator();
		while(petSitterServices.hasNext()){
			PetSitterServiceRequest serviceRequest = petSitterServices.next();
			WithPetService withPetService = withPetServiceRepository.findById(
					serviceRequest.getServiceId()).orElseThrow(()->{
						throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND,
								ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
			});
			PetSitterWithPetService petSitterWithPetService = PetSitterWithPetService.toEntity(withPetService,petSitter,serviceRequest.getPrice());
			petSitterServiceRepository.save(petSitterWithPetService);
		}
		petSitter.updateIntroduction(petSitterModifyInfoRequest.getIntroduction());
		// 펫시터 정보를 입력한 다음 validation이 true로 바뀌어서 Main page에 조회가 된다.
		petSitter.changeValidation(true);

		Iterator<PetSitterCriticalServiceRequest> petSitterCriticalServices = petSitterModifyInfoRequest.getPetSitterCriticalServiceRequests().iterator();

		while(petSitterCriticalServices.hasNext()){
			PetSitterCriticalServiceRequest criticalServiceRequest = petSitterCriticalServices.next();
			CriticalService criticalService = criticalServiceRepository.findById(
					criticalServiceRequest.getServiceId()).orElseThrow(()->{
						throw new AppException(ErrorCode.CRITICAL_SERVICE_NOT_FOUND, ErrorCode.CRITICAL_SERVICE_NOT_FOUND.getMessage());
					});
			if(criticalServiceRequest.getServiceId() == 1){
				petSitter.changeAvailableDogSize(DogSize.소형견);
			}else if(criticalServiceRequest.getServiceId() == 2){
				petSitter.changeAvailableDogSize(DogSize.중형견);
			}else{
				petSitter.changeAvailableDogSize(DogSize.대형견);
			}
			PetSitterCriticalService petSitterCriticalService = PetSitterCriticalService.toEntity(criticalService, petSitter,criticalServiceRequest.getPrice());
			petSitterCriticalServiceRepository.save(petSitterCriticalService);
		}

		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();
		List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();

		return PetSitterModifyInfoResponse.of(petSitter, criticalServiceList, withPetServiceList);
	}

	// == 메인페이지 펫시터들 정보 조회 == //
	// 리팩토링 무조건 무조건 필요 .. 쿼리가 너무 많이 나간다.
	// userName을 불러오기 위해서 user테이블에서 조회하는 쿼리가 생기는데 이를 해결해야 한다..
	public Page getPetSitters(Pageable pageable){
		Page<PetSitter> petSitters = petSitterRepository.searchPage(pageable);
		return petSitters.map(PetSitterMainResponse::of);
	}
}
