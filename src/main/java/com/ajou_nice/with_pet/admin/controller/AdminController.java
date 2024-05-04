package com.ajou_nice.with_pet.admin.controller;


import com.ajou_nice.with_pet.admin.model.criticalservice.AddCriticalServiceRequest;
import com.ajou_nice.with_pet.critical_service.model.dto.CriticalServiceResponse;
import com.ajou_nice.with_pet.admin.model.withpetservice.AddWithPetServiceRequest;
import com.ajou_nice.with_pet.admin.model.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.admin.service.AdminService;
import com.ajou_nice.with_pet.domain.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/admins")
@Api(tags = "Administrator API")
public class AdminController {

	private final AdminService adminService;
//배포를 위한 주석 추가
//	@PostMapping("/api/v1/admin/accept-petsitter")
//	@ApiOperation(value = "관리자의 펫시터 지원자 수락")
//	public Response<PetSitterBasicResponse> acceptApplicant(@ApiIgnore Authentication authentication, @RequestBody @Valid AdminApplicantRequest adminApplicantRequest){
//
//		log.info("=============== accept petsitter info : {} ==================",
//				adminApplicantRequest);
//		PetSitterBasicResponse adminAcceptApplicantResponse = adminService.createPetsitter(
//				authentication.getName(), adminApplicantRequest);
//
//		log.info("=============== accepted petsitter info : {} =================", adminAcceptApplicantResponse);
//		return Response.success(adminAcceptApplicantResponse);
//	}

//	@GetMapping("/api/v1/admin/show-petsitters")
//	@ApiOperation(value = "관리자의 펫시터 리스트 조회")
//	public Response<List<PetSitterBasicResponse>> showPetSitters(@ApiIgnore Authentication authentication){
//		log.info(authentication.getName());
//		List<PetSitterBasicResponse> petSitterBasicResponses = adminService.showPetSitters(
//				authentication.getName());
//		return Response.success(petSitterBasicResponses);
//	}

//	@GetMapping("/api/v1/show-applicants")
//	@ApiOperation(value = "펫시터 지원자 리스트 전체 확인")
//	public Response<List<ApplicantBasicInfoResponse>> showApplicants(@ApiIgnore Authentication authentication){
//
//		List<ApplicantBasicInfoResponse> applicantList = adminService.showApplicants(
//				authentication.getName());
//		log.info("===================applicant Info List Response : {} ==================", applicantList);
//		return Response.success(applicantList);
//	}

//	@GetMapping("/api/v1/show-applicant/{userId}")
//	@ApiOperation(value = "펫시터 지원자 정보 상세 확인")
//	public Response<ApplicantCreateResponse> getApplicant(@ApiIgnore Authentication authentication, @PathVariable("userId")Long userId){
//
//		ApplicantCreateResponse applicantCreateResponse = adminService.getApplicantInfo(
//				authentication.getName(), userId);
//
//		return Response.success(applicantCreateResponse);
//	}

//	@PostMapping("/api/v1/admin/refuse-applicant")
//	@ApiOperation(value = "관리자의 펫시터 지원자 거절")
//	public Response<AdminApplicantResponse> refuseApplicant(@ApiIgnore Authentication authentication, @RequestBody @Valid AdminApplicantRequest adminApplicantRequest){
//		log.info("=============== refuse petsitter info : {} ==================",adminApplicantRequest);
//
//		AdminApplicantResponse adminApplicantResponse = adminService.refuseApplicant(
//				authentication.getName(), adminApplicantRequest);
//
//		log.info("=============== refused petsitter info : {} =================", adminApplicantResponse);
//
//		return Response.success(adminApplicantResponse);
//	}

	@GetMapping("/services")
	@ApiOperation(value = "위드펫 서비스 리스트 조회")
	public Response<List<WithPetServiceResponse>> showWithPetServices(){
		List<WithPetServiceResponse> withPetServiceList = adminService.showWithPetServices();
		return Response.success(withPetServiceList);
	}

	@PostMapping("/critical-service")
	@ApiOperation(value = "관리자의 필수 서비스 추가")
	public Response<CriticalServiceResponse> addCriticalService(@ApiIgnore Authentication authentication, @RequestBody @Valid
	AddCriticalServiceRequest addCriticalServiceRequest){
		CriticalServiceResponse criticalServiceResponse = adminService.addCriticalService(
				authentication.getName(), addCriticalServiceRequest);
		return Response.success(criticalServiceResponse);
	}

//	@PutMapping("/api/v1/admin/criticalservice")
//	@ApiOperation(value = "관리자의 필수 서비스 수정")
//	public Response<CriticalServiceResponse> updateCriticalService(@ApiIgnore Authentication authentication, @RequestBody @Valid
//	CriticalServiceRequest.CriticalServiceModifyRequest criticalServiceModifyRequest){
//		CriticalServiceResponse criticalServiceResponse = adminService.updateCriticalService(
//				authentication.getName(), criticalServiceModifyRequest);
//
//		return Response.success(criticalServiceResponse);
//	}

	@PostMapping("/service")
	@ApiOperation(value = "관리자의 위드펫 서비스 추가")
	public Response<WithPetServiceResponse> addWithPetService(@ApiIgnore Authentication authentication, @RequestBody @Valid
	AddWithPetServiceRequest addWithPetServiceRequest){
		WithPetServiceResponse withPetServiceResponse = adminService.addWithPetService(
				authentication.getName(), addWithPetServiceRequest);
		return Response.success(withPetServiceResponse);
	}

//	@PutMapping("/api/v1/admin/service")
//	@ApiOperation(value = "관리자의 위드펫 서비스 수정")
//	public Response<WithPetServiceResponse> updateWithPetService(@ApiIgnore Authentication authentication,@RequestBody @Valid
//			WithPetServiceModifyRequest withPetServiceModifyRequest){
//
//		log.info("=============== request update withPetService info : {} ==================",withPetServiceModifyRequest);
//
//		WithPetServiceResponse withPetServiceResponse = adminService.updateWithPetService(
//				authentication.getName(), withPetServiceModifyRequest);
//
//		log.info("=============== response update withPetService info : {} ==================",withPetServiceResponse);
//
//		return Response.success(withPetServiceResponse);
//	}
//
//	@PostMapping  ("/api/v1/admin/service")
//	@ApiOperation(value = "관리자의 위드펫 서비스 삭제")
//	public Response<List<WithPetServiceResponse>> deleteWithPetService(@ApiIgnore Authentication authentication,@RequestBody @Valid
//			WithPetServiceModifyRequest withPetServiceModifyRequest){
//
//		log.info("=============== request delete withPetService info : {} ==================",withPetServiceModifyRequest);
//
//		List<WithPetServiceResponse> withPetServiceResponses = adminService.deleteWithPetService(
//				authentication.getName(), withPetServiceModifyRequest);
//
//		log.info("=============== response deleted withPetService List : {} ==================",withPetServiceResponses);
//
//		return Response.success(withPetServiceResponses);
//	}
}
