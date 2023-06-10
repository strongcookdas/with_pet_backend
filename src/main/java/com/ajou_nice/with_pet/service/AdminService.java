package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantRequest;
import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantResponse;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceRequest;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceRequest.CriticalServiceModifyRequest;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterBasicResponse;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantBasicInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoResponse;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceRequest;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceRequest.WithPetServiceModifyRequest;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.repository.WithPetServiceRepository;
import com.ajou_nice.with_pet.service.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final UserRepository userRepository;
	private final PetSitterRepository petSitterRepository;
	private final WithPetServiceRepository withPetServiceRepository;

	private final UserService userService;

	private final CriticalServiceRepository criticalServiceRepository;

	private final ValidateCollection valid;

	// == 관리자의 펫시터 지원자 리스트 전체 확인 == //
	// 리팩토링 완 -> Authentication자체가 없어도 되는지도 확인 필요 (나중에 권한 설정한 다음에)
	public List<ApplicantBasicInfoResponse> showApplicants(String userId){

		valid.userValidation(userId);

		List<User> petSitterApplicantList = userRepository.findApplicantAllInQuery(UserRole.ROLE_APPLICANT, ApplicantStatus.WAIT);

		return ApplicantBasicInfoResponse.toList(petSitterApplicantList);
	}

	// == 관리자의 펫시터 리스트 전체 확인 == //
	public List<PetSitterBasicResponse> showPetSitters(String userId){
		valid.userValidation(userId);
		List<PetSitter> petSitters = petSitterRepository.findAll();

		return PetSitterBasicResponse.toList(petSitters);
	}

	// == 관리자의 펫시터 지원자 수락 == //
	// 리팩토링 완 -> Authentication 자체가 없어도 되는지도 확인 필요 (나중에 권한 설정한 다음에)
	@Transactional
	public PetSitterBasicResponse createPetsitter(String userId, AdminApplicantRequest adminApplicantRequest){

		valid.userValidation(userId);

		//유저 불러오기
		User findUser = userRepository.findByUserId(adminApplicantRequest.getUserId()).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});
		//펫시터 지원자 상태 변경 + userRole 변경
		findUser.updateApplicantStatus(ApplicantStatus.APPROVE);
		findUser.updateUserRole(UserRole.ROLE_PETSITTER);

		PetSitter petSitter = PetSitter.toEntity(findUser);
		PetSitter newPetSitter = petSitterRepository.save(petSitter);

		return PetSitterBasicResponse.of(newPetSitter);
	}

	// == 관리자의 펫시터 지원자 거절 == //
	// 리팩토링 완 -> Authentication 자체가 없어도 되는지도 확인 필요 (나중에 권한 설정한 다음에)
	@Transactional
	public AdminApplicantResponse refuseApplicant(String userId, AdminApplicantRequest adminApplicantRequest){

		valid.userValidation(userId);

		//유저 불러오기
		User findUser = userRepository.findByUserId(adminApplicantRequest.getUserId()).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		//펫시터 지원자 상태 변경 + userRole 변경
		findUser.updateApplicantStatus(ApplicantStatus.REFUSE);
		findUser.updateUserRole(UserRole.ROLE_USER);

		return AdminApplicantResponse.of(findUser);
	}


	// == 관리자의 펫시터 지원자 한명 상세정보 확인 == //
	// 리팩토링 완
	public ApplicantInfoResponse getApplicantInfo(String id, Long userId){

		valid.userValidation(id);

		//유저 불러오기
		User findUser = userRepository.findByUserId(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});


		return ApplicantInfoResponse.ofAll(findUser);
	}

	// == 관리자의 위드펫 서비스 리스트 조회 == //
	public List<WithPetServiceResponse> showWithPetServices(){

		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

		return WithPetServiceResponse.toList(withPetServiceList);
	}

	// == 관리자의 필수 서비스 리스트 조회 == //
	public List<CriticalServiceResponse> showCriticalServices(String userId){
		valid.userValidation(userId);
		List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();

		return CriticalServiceResponse.toList(criticalServiceList);
	}

	@Transactional
	// == 관리자의 필수 서비스 등록 == //
	public CriticalServiceResponse addCriticalService(String userId, CriticalServiceRequest criticalServiceRequest){
		valid.userValidation(userId);

		List<CriticalService> criticalServiceList = criticalServiceRepository.findCritiCalServiceByServiceName(
				criticalServiceRequest.getServiceName());
		if(!criticalServiceList.isEmpty()){
			throw new AppException(ErrorCode.DUPlICATED_SERVICE, ErrorCode.DUPlICATED_SERVICE.getMessage());
		}
		CriticalService criticalService = CriticalService.toEntity(criticalServiceRequest);
		CriticalService newCriticalService = criticalServiceRepository.save(criticalService);

		return CriticalServiceResponse.of(newCriticalService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 등록 == //
	public WithPetServiceResponse addWithPetService(String userId, WithPetServiceRequest withPetServiceRequest){
		valid.userValidation(userId);
		WithPetService withPetService = WithPetService.toEntity(withPetServiceRequest);
		WithPetService newWithPetService = withPetServiceRepository.save(withPetService);

		return WithPetServiceResponse.of(newWithPetService);
	}

	@Transactional
	// == 관리자의 필수 서비스 수정 == //
	public CriticalServiceResponse updateCriticalService(
			String userId, CriticalServiceModifyRequest criticalServiceModifyRequest){
		valid.userValidation(userId);
		CriticalService criticalService = valid.criticalServiceValidation(
				criticalServiceModifyRequest.getServiceId());
		criticalService.updateServiceInfo(criticalServiceModifyRequest);

		return CriticalServiceResponse.of(criticalService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 수정 == //
	public WithPetServiceResponse updateWithPetService(String userId, WithPetServiceModifyRequest withPetServiceModifyRequest){
		valid.userValidation(userId);
		WithPetService withPetService = valid.withPetServiceValidation(
				withPetServiceModifyRequest.getServiceId());
		withPetService.updateServiceInfo(withPetServiceModifyRequest);

		return WithPetServiceResponse.of(withPetService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 삭제 == //
	public List<WithPetServiceResponse> deleteWithPetService(String userId, WithPetServiceModifyRequest withPetServiceModifyRequest){
		valid.userValidation(userId);
		WithPetService withPetService = valid.withPetServiceValidation(
				withPetServiceModifyRequest.getServiceId());

		withPetServiceRepository.delete(withPetService);
		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

		return WithPetServiceResponse.toList(withPetServiceList);
	}
}
