package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.petsitter.CreateServiceRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterInfoResponse;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PetSitterApplicantRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.PetSitterServiceRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetSitterService {

	private final PetSitterRepository petSitterRepository;
	private final PetSitterServiceRepository petSitterServiceRepository;
	private final PetSitterApplicantRepository petSitterApplicantRepository;
	private final UserRepository userRepository;


	@Transactional
	// == 펫시터의 서비스 선택 및 등록 == //
	public PetSitterInfoResponse updatePetSitterService(String userId, CreateServiceRequest createServiceRequest){

		//리팩토링 필요 ㅠ -> 쿼리 dsl로 쿼리문으로 한번에 가져오는 게 필요하다.
		//petSitter 레포에서 userId로 petsitter를 find할 수 있는 쿼리문 필요 -> 한줄에 끝낼 수 있다.
		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		PetSitterApplicant findApplicant = petSitterApplicantRepository.findByUser(findUser).orElseThrow(()->{
			throw new AppException(ErrorCode.APPLICANT_NOT_FOUND, ErrorCode.APPLICANT_NOT_FOUND.getMessage());
		});

		PetSitter petSitter = petSitterRepository.findByApplicant(findApplicant).orElseThrow(()->{
			throw new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage());
		});

		Map<WithPetService, Integer> withPetServicePriceMap = createServiceRequest.getWithPetServicePriceMap();
		Iterator<WithPetService> services = withPetServicePriceMap.keySet().iterator();
		while(services.hasNext()){
			WithPetService service = services.next();
			int price = withPetServicePriceMap.get(service).intValue();
			PetSitterWithPetService withPetService = PetSitterWithPetService.toEntity(service, petSitter, price);
			petSitterServiceRepository.save(withPetService);
		}

		return PetSitterInfoResponse.of(petSitter);
	}

	// == 메인페이지 펫시터들 정보 조회 == //
	public List<PetSitterInfoResponse> getPetSitters(){

	}


}
