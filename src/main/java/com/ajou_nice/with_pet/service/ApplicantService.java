package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantBasicInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest.ApplicantModifyRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoResponse;
import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PetSitterApplicantRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {

	private final PetSitterApplicantRepository petSitterApplicantRepository;
	private final UserRepository userRepository;


	// == 유저의 펫시터 지원자 등록 == //
	public ApplicantInfoResponse registerApplicant(ApplicantInfoRequest applicantInfoRequest, String userId){
		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		// 유저 펫시터 지원 중복 체크
		if(petSitterApplicantRepository.findByUser(findUser).isPresent()){
			throw new AppException(ErrorCode.DUPLICATED_APPLICATION, ErrorCode.DUPLICATED_APPLICATION.getMessage());
		}

		PetSitterApplicant petSitterApplicant = PetSitterApplicant.toEntity(applicantInfoRequest, findUser);
		PetSitterApplicant newPetSitterApplicant = petSitterApplicantRepository.save(petSitterApplicant);

		return ApplicantInfoResponse.ofAll(newPetSitterApplicant);
	}

	// == 유저의 지원 정보 상세 확인 == //
	public ApplicantInfoResponse showApplicateInfo(String userId){
		User finduser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		PetSitterApplicant petSitterApplicant = petSitterApplicantRepository.findByUser(finduser).orElseThrow(()->{
			throw new AppException(ErrorCode.APPLICANT_NOT_FOUND, ErrorCode.APPLICANT_NOT_FOUND.getMessage());
		});

		return ApplicantInfoResponse.ofAll(petSitterApplicant);
	}

	// == 유저의 펫시터 지원서류 수정 == //
	@Transactional
	public ApplicantInfoResponse modifyApplicateInfo(String userId, ApplicantModifyRequest applicantModifyRequest){
		User finduser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});
		PetSitterApplicant findApplicant = petSitterApplicantRepository.findByUser(finduser).orElseThrow(()->{
			throw new AppException(ErrorCode.APPLICANT_NOT_FOUND, ErrorCode.APPLICANT_NOT_FOUND.getMessage());
		});
		// 지원 정보 수정
		findApplicant.updateApplicateInfo(applicantModifyRequest);

		return ApplicantInfoResponse.ofAll(findApplicant);
	}

}