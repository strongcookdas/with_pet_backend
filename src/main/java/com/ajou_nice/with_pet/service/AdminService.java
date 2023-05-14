package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantRequest;
import com.ajou_nice.with_pet.domain.dto.admin.AdminAcceptApplicantResponse;
import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantResponse;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceRequest;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceRequest.CriticalServiceModifyRequest;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceResponse;
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

	private final CriticalServiceRepository criticalServiceRepository;

	// == 관리자의 펫시터 지원자 리스트 전체 확인 == //
	public List<ApplicantBasicInfoResponse> showApplicants(){
		List<PetSitterApplicant> petSitterApplicantList = petSitterApplicantRepository.findAll();

		return ApplicantBasicInfoResponse.toList(petSitterApplicantList);
	}

	// == 관리자의 펫시터 지원자 수락 == //
	@Transactional
	public AdminAcceptApplicantResponse createPetsitter(AdminApplicantRequest adminApplicantRequest){


		//APPROVE 로 들어와야 한다.
		if(adminApplicantRequest.getApplicantStatus() != ApplicantStatus.APPROVE){
			throw new IllegalArgumentException(ErrorCode.BAD_REQUEST_APPLICANT_STATUS.getMessage());
		}

		//펫시터 불러오기
		PetSitterApplicant petSitterApplicant = petSitterApplicantRepository.findById(
				adminApplicantRequest.getApplicantId()).orElseThrow(()->{
					throw new AppException(ErrorCode.APPLICANT_NOT_FOUND, ErrorCode.APPLICANT_NOT_FOUND.getMessage());
		});
		//펫시터 지원자 상태 변경 + userRole 변경
		petSitterApplicant.updateApplicantState(adminApplicantRequest.getApplicantStatus());
		//유저 불러오기
		User findUser = userRepository.findById(adminApplicantRequest.getApplicant_userId()).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});
		findUser.updateUserRole(UserRole.ROLE_PETSITTER);

		PetSitter petSitter = PetSitter.toEntity(petSitterApplicant);
		PetSitter newPetSitter = petSitterRepository.save(petSitter);

		return AdminAcceptApplicantResponse.ofAccept(newPetSitter);

	}
	// == 관리자의 펫시터 지원자 거절 == //
	@Transactional
	public AdminApplicantResponse refuseApplicant(AdminApplicantRequest adminApplicantRequest){

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
	public ApplicantInfoResponse getApplicantInfo(Long applicantId){
		PetSitterApplicant petSitterApplicant = petSitterApplicantRepository.findById(applicantId).orElseThrow(()->{
			throw new AppException(ErrorCode.APPLICANT_NOT_FOUND, ErrorCode.APPLICANT_NOT_FOUND.getMessage());
		});

		return ApplicantInfoResponse.ofAll(petSitterApplicant);
	}

	// == 관리자의 위드펫 서비스 리스트 조회 == //
	public List<WithPetServiceResponse> showWithPetServices(){
		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

		return WithPetServiceResponse.toList(withPetServiceList);
	}

	// == 관리자의 필수 서비스 리스트 조회 == //
	public List<CriticalServiceResponse> showCriticalServices(){
		List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();

		return CriticalServiceResponse.toList(criticalServiceList);
	}

	@Transactional
	// == 관리자의 필수 서비스 등록 == //
	public CriticalServiceResponse addCriticalService(CriticalServiceRequest criticalServiceRequest){
		CriticalService criticalService = CriticalService.toEntity(criticalServiceRequest);
		CriticalService newCriticalService = criticalServiceRepository.save(criticalService);

		return CriticalServiceResponse.of(newCriticalService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 등록 == //
	public WithPetServiceResponse addWithPetService(WithPetServiceRequest withPetServiceRequest){
		WithPetService withPetService = WithPetService.toEntity(withPetServiceRequest);
		WithPetService newWithPetService = withPetServiceRepository.save(withPetService);

		return WithPetServiceResponse.of(newWithPetService);
	}

	@Transactional
	// == 관리자의 필수 서비스 수정 == //
	public CriticalServiceResponse updateCriticalService(
			CriticalServiceModifyRequest criticalServiceModifyRequest){
		CriticalService criticalService = criticalServiceRepository.findById(criticalServiceModifyRequest.getServiceId())
				.orElseThrow(()->{
					throw new AppException(ErrorCode.CRITICAL_SERVICE_NOT_FOUND, ErrorCode.CRITICAL_SERVICE_NOT_FOUND.getMessage());
				});
		criticalService.updateServiceInfo(criticalServiceModifyRequest);

		return CriticalServiceResponse.of(criticalService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 수정 == //
	public WithPetServiceResponse updateWithPetService(WithPetServiceModifyRequest withPetServiceModifyRequest){
		WithPetService withPetService = withPetServiceRepository.findById(withPetServiceModifyRequest.getServiceId())
				.orElseThrow(()->{
					throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND, ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
		});
		withPetService.updateServiceInfo(withPetServiceModifyRequest);

		return WithPetServiceResponse.of(withPetService);
	}

	@Transactional
	// == 관리자의 위드펫에서 제공하는 서비스 삭제 == //
	public List<WithPetServiceResponse> deleteWithPetService(WithPetServiceModifyRequest withPetServiceModifyRequest){
		WithPetService withPetService = withPetServiceRepository.findById(
				withPetServiceModifyRequest.getServiceId()).orElseThrow(()->{
					throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND, ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
		});
		List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

		return WithPetServiceResponse.toList(withPetServiceList);
	}
}
