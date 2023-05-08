package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoResponse;
import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PetSitterApplicantRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicantService {

	private final PetSitterApplicantRepository petSitterApplicantRepository;
	private final UserRepository userRepository;

	// == 펫시터 지원자 등록 == //
	public ApplicantInfoResponse registerApplicant(ApplicantInfoRequest applicantInfoRequest, String userId){
		User finduser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});
		PetSitterApplicant petSitterApplicant = PetSitterApplicant.of(applicantInfoRequest, finduser);
		PetSitterApplicant newPetSitterApplicant = petSitterApplicantRepository.save(petSitterApplicant);

		return ApplicantInfoResponse.of(newPetSitterApplicant);
	}

	//유저의 펫시터 지원

	//유저의 지원 상태 확인



	//관리자는 펫시터가 제공할 수 있는 서비스를 추가할 수 있다.

	//관리자는 펫시터가 제공할 수 있는 서비스를 수정할 수 있다.(이미지, 이름 등)

}