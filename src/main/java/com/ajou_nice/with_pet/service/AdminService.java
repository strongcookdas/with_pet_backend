package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantRequest;
import com.ajou_nice.with_pet.domain.dto.admin.AdminAcceptApplicantResponse;
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
import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.repository.PetSitterApplicantRepository;
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

	private final PetSitterApplicantRepository petSitterApplicantRepository;
	private final UserRepository userRepository;
	private final PetSitterRepository petSitterRepository;
	private final WithPetServiceRepository withPetServiceRepository;

	private final UserService userService;

	private final CriticalServiceRepository criticalServiceRepository;

	// == 관리자의 펫시터 지원자 리스트 전체 확인 == //
	public List<ApplicantBasicInfoResponse> showApplicants(String userId){

		User findUser = userService.findUser(userId);
		List<PetSitterApplicant> petSitterApplicantList = petSitterApplicantRepository.findAllInQuery(ApplicantStatus.WAIT);

		return ApplicantBasicInfoResponse.toList(petSitterApplicantList);
	}

	// == 관리자의 펫시터 리스트 전체 확인 == //
	public List<PetSitterBasicResponse> showPetSitters(String userId){
		User findUser = userService.findUser(userId);
		List<PetSitter> petSitters = petSitterRepository.findAll();

		return PetSitterBasicResponse.toList(petSitters);
	}

	// == 관리자의 펫시터 지원자 수락 == //
	@Transactional
	public PetSitterBasicResponse createPetsitter(String userId, AdminApplicantRequest adminApplicantRequest){
		User findAdminUser = userService.findUser(userId);

		adminApplicantRequest.setApplicantStatus(ApplicantStatus.APPROVE);

		//펫시터 불러오기
		PetSitterApplicant petSitterApplicant = petSitterApplicantRepository.findById(
				adminApplicantRequest.getApplicantId()).orElseThrow(()->{
					throw new AppException(ErrorCode.APPLICANT_NOT_FOUND, ErrorCode.APPLICANT_NOT_FOUND.getMessage());
		});
		//펫시터 지원자 상태 변경 + userRole 변경
		petSitterApplicant.updateApplicantState(adminApplicantRequest.getApplicantStatus());
		//유저 불러오기
		User findUser = userRepository.findByUserId(adminApplicantRequest.getApplicant_userId()).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});
		findUser.updateUserRole(UserRole.ROLE_PETSITTER);

		PetSitter petSitter = PetSitter.toEntity(petSitterApplicant);
		PetSitter newPetSitter = petSitterRepository.save(petSitter);

		return PetSitterBasicResponse.of(newPetSitter);

	}
	// == 관리자의 펫시터 지원자 거절 == //
	@Transactional
	public AdminApplicantResponse refuseApplicant(String userId, AdminApplicantRequest adminApplicantRequest){
		User findUser = userService.findUser(userId);
		adminApplicantRequest.setApplicantStatus(ApplicantStatus.REFUSE);

		//Refuse로 들어와야 한다.
		if(adminApplicantRequest.getApplicantStatus() != ApplicantStatus.REFUSE){
			throw new IllegalArgumentException(ErrorCode.BAD_REQUEST_APPLICANT_STATUS.getMessage());
		}
		//펫시터 지원자 불러오기
		PetSitterApplicant findApplicant = petSitterApplicantRepository.findById(
				adminApplicantRequest.getApplicantId()).orElseThrow(()->{
			throw new AppException(ErrorCode.APPLICANT_NOT_FOUND, ErrorCode.APPLICANT_NOT_FOUND.getMessage());
		});
		findApplicant.updateApplicantState(adminApplicantRequest.getApplicantStatus());

		return AdminApplicantResponse.of(findApplicant);
	}


	// == 관리자의 펫시터 지원자 한명 상세정보 확인 == //
	public ApplicantInfoResponse getApplicantInfo(String userId, Long applicantId){
		User findUser = userService.findUser(userId);
		PetSitterApplicant petSitterApplicant = petSitterApplicantRepository.findById(applicantId).orElseThrow(()->{
			throw new AppException(ErrorCode.APPLICANT_NOT_FOUND, ErrorCode.APPLICANT_NOT_FOUND.getMessage());
		});

		return ApplicantInfoResponse.ofAll(petSitterApplicant);
	}

	// == 관리자의 위드펫 서비스 리스트 조회 == //
	public List<WithPetServiceResponse> showWithPetServices(String userId){
		User findUser = userService.findUser(userId);
		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

		return WithPetServiceResponse.toList(withPetServiceList);
	}

	// == 관리자의 필수 서비스 리스트 조회 == //
	public List<CriticalServiceResponse> showCriticalServices(String userId){
		User findUser = userService.findUser(userId);
		List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();

		return CriticalServiceResponse.toList(criticalServiceList);
	}

	@Transactional
	// == 관리자의 필수 서비스 등록 == //
	public CriticalServiceResponse addCriticalService(String userId, CriticalServiceRequest criticalServiceRequest){
		User findUser = userService.findUser(userId);

		CriticalService findCriticalService = criticalServiceRepository.findCritiCalServiceByServiceName(
				criticalServiceRequest.getServiceName()).orElseThrow(()->{
					throw new AppException(ErrorCode.DUPlICATED_SERVICE, ErrorCode.DUPlICATED_SERVICE.getMessage());
		});
		CriticalService criticalService = CriticalService.toEntity(criticalServiceRequest);
		CriticalService newCriticalService = criticalServiceRepository.save(criticalService);

		return CriticalServiceResponse.of(newCriticalService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 등록 == //
	public WithPetServiceResponse addWithPetService(String userId, WithPetServiceRequest withPetServiceRequest){
		User findUser = userService.findUser(userId);
		WithPetService withPetService = WithPetService.toEntity(withPetServiceRequest);
		WithPetService newWithPetService = withPetServiceRepository.save(withPetService);

		return WithPetServiceResponse.of(newWithPetService);
	}

	@Transactional
	// == 관리자의 필수 서비스 수정 == //
	public CriticalServiceResponse updateCriticalService(
			String userId, CriticalServiceModifyRequest criticalServiceModifyRequest){
		User findUser = userService.findUser(userId);
		CriticalService criticalService = criticalServiceRepository.findById(criticalServiceModifyRequest.getServiceId())
				.orElseThrow(()->{
					throw new AppException(ErrorCode.CRITICAL_SERVICE_NOT_FOUND, ErrorCode.CRITICAL_SERVICE_NOT_FOUND.getMessage());
				});
		criticalService.updateServiceInfo(criticalServiceModifyRequest);

		return CriticalServiceResponse.of(criticalService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 수정 == //
	public WithPetServiceResponse updateWithPetService(String userId, WithPetServiceModifyRequest withPetServiceModifyRequest){
		User findUser = userService.findUser(userId);
		WithPetService withPetService = withPetServiceRepository.findById(withPetServiceModifyRequest.getServiceId())
				.orElseThrow(()->{
					throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND, ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
		});
		withPetService.updateServiceInfo(withPetServiceModifyRequest);

		return WithPetServiceResponse.of(withPetService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 삭제 == //
	public List<WithPetServiceResponse> deleteWithPetService(String userId, WithPetServiceModifyRequest withPetServiceModifyRequest){
		User findUser = userService.findUser(userId);
		WithPetService withPetService = withPetServiceRepository.findById(
				withPetServiceModifyRequest.getServiceId()).orElseThrow(()->{
					throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND, ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
		});

		withPetServiceRepository.delete(withPetService);
		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

		return WithPetServiceResponse.toList(withPetServiceList);
	}
}
