package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantRequest;
import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantResponse;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceRequest;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterBasicResponse;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantBasicInfoResponse;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateResponse;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceRequest;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceRequest.WithPetServiceModifyRequest;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "Administrator api")
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/api/v1/admin/accept-petsitter")
	@ApiOperation(value = "관리자의 펫시터 지원자 수락")
	public Response<PetSitterBasicResponse> acceptApplicant(@ApiIgnore Authentication authentication, @RequestBody @Valid AdminApplicantRequest adminApplicantRequest){

		log.info("=============== accept petsitter info : {} ==================",
				adminApplicantRequest);
		PetSitterBasicResponse adminAcceptApplicantResponse = adminService.createPetsitter(
				authentication.getName(), adminApplicantRequest);

		log.info("=============== accepted petsitter info : {} =================", adminAcceptApplicantResponse);
		return Response.success(adminAcceptApplicantResponse);
	}

	@GetMapping("/api/v1/admin/show-petsitters")
	@ApiOperation(value = "관리자의 펫시터 리스트 조회")
	public Response<List<PetSitterBasicResponse>> showPetSitters(@ApiIgnore Authentication authentication){
		log.info(authentication.getName());
		List<PetSitterBasicResponse> petSitterBasicResponses = adminService.showPetSitters(
				authentication.getName());
		return Response.success(petSitterBasicResponses);
	}

	@GetMapping("/api/v1/show-applicants")
	@ApiOperation(value = "펫시터 지원자 리스트 전체 확인")
	public Response<List<ApplicantBasicInfoResponse>> showApplicants(@ApiIgnore Authentication authentication){

		List<ApplicantBasicInfoResponse> applicantList = adminService.showApplicants(
				authentication.getName());
		log.info("===================applicant Info List Response : {} ==================", applicantList);
		return Response.success(applicantList);
	}

	@GetMapping("/api/v1/show-applicant/{userId}")
	@ApiOperation(value = "펫시터 지원자 정보 상세 확인")
	public Response<ApplicantCreateResponse> getApplicant(@ApiIgnore Authentication authentication, @PathVariable("userId")Long userId){

		ApplicantCreateResponse applicantCreateResponse = adminService.getApplicantInfo(
				authentication.getName(), userId);

		return Response.success(applicantCreateResponse);
	}

	@PostMapping("/api/v1/admin/refuse-applicant")
	@ApiOperation(value = "관리자의 펫시터 지원자 거절")
	public Response<AdminApplicantResponse> refuseApplicant(@ApiIgnore Authentication authentication, @RequestBody @Valid AdminApplicantRequest adminApplicantRequest){
		log.info("=============== refuse petsitter info : {} ==================",adminApplicantRequest);

		AdminApplicantResponse adminApplicantResponse = adminService.refuseApplicant(
				authentication.getName(), adminApplicantRequest);

		log.info("=============== refused petsitter info : {} =================", adminApplicantResponse);

		return Response.success(adminApplicantResponse);
	}

	@GetMapping("/api/v1/show-services")
	@ApiOperation(value = "위드펫 서비스 리스트 조회")
	public Response<List<WithPetServiceResponse>> showWithPetServices(){
		List<WithPetServiceResponse> withPetServiceList = adminService.showWithPetServices();

		log.info("=============== withPet service list : {} ================", withPetServiceList);

		return Response.success(withPetServiceList);
	}

	@GetMapping("/api/v1/show-criticalservices")
	@ApiOperation(value = "관리자의 필수 위드펫 서비스 리스트 조회")
	public Response<List<CriticalServiceResponse>> showCriticalServices(@ApiIgnore Authentication authentication){
		List<CriticalServiceResponse> criticalServiceResponseList = adminService.showCriticalServices(
				authentication.getName());

		return Response.success(criticalServiceResponseList);
	}

	@PostMapping("/api/v1/admin/add-criticalservice")
	@ApiOperation(value = "관리자의 필수 서비스 추가")
	public Response<CriticalServiceResponse> addCriticalService(@ApiIgnore Authentication authentication, @RequestBody @Valid
			CriticalServiceRequest criticalServiceRequest){
		log.info("=============== refuse petsitter info : {} ==================",criticalServiceRequest);
		CriticalServiceResponse criticalServiceResponse = adminService.addCriticalService(
				authentication.getName(),criticalServiceRequest);


		log.info("=============== refuse petsitter info : {} ==================",criticalServiceResponse);

		return Response.success(criticalServiceResponse);
	}

	@PutMapping("/api/v1/admin/criticalservice")
	@ApiOperation(value = "관리자의 필수 서비스 수정")
	public Response<CriticalServiceResponse> updateCriticalService(@ApiIgnore Authentication authentication, @RequestBody @Valid
	CriticalServiceRequest.CriticalServiceModifyRequest criticalServiceModifyRequest){
		CriticalServiceResponse criticalServiceResponse = adminService.updateCriticalService(
				authentication.getName(), criticalServiceModifyRequest);

		return Response.success(criticalServiceResponse);
	}

	@PostMapping("/api/v1/admin/add-service")
	@ApiOperation(value = "관리자의 위드펫 서비스 추가")
	public Response<WithPetServiceResponse> addWithPetService(@ApiIgnore Authentication authentication, @RequestBody @Valid
			WithPetServiceRequest withPetServiceRequest){

		log.info("=============== request create withPetService info : {} ==================",withPetServiceRequest);
		WithPetServiceResponse withPetServiceResponse = adminService.addWithPetService(
				authentication.getName(), withPetServiceRequest);

		log.info("=============== response create withPetService info : {} ==================",withPetServiceResponse);

		return Response.success(withPetServiceResponse);
	}

	@PutMapping("/api/v1/admin/service")
	@ApiOperation(value = "관리자의 위드펫 서비스 수정")
	public Response<WithPetServiceResponse> updateWithPetService(@ApiIgnore Authentication authentication,@RequestBody @Valid
			WithPetServiceModifyRequest withPetServiceModifyRequest){

		log.info("=============== request update withPetService info : {} ==================",withPetServiceModifyRequest);

		WithPetServiceResponse withPetServiceResponse = adminService.updateWithPetService(
				authentication.getName(), withPetServiceModifyRequest);

		log.info("=============== response update withPetService info : {} ==================",withPetServiceResponse);

		return Response.success(withPetServiceResponse);
	}

	@PostMapping  ("/api/v1/admin/service")
	@ApiOperation(value = "관리자의 위드펫 서비스 삭제")
	public Response<List<WithPetServiceResponse>> deleteWithPetService(@ApiIgnore Authentication authentication,@RequestBody @Valid
			WithPetServiceModifyRequest withPetServiceModifyRequest){

		log.info("=============== request delete withPetService info : {} ==================",withPetServiceModifyRequest);

		List<WithPetServiceResponse> withPetServiceResponses = adminService.deleteWithPetService(
				authentication.getName(), withPetServiceModifyRequest);

		log.info("=============== response deleted withPetService List : {} ==================",withPetServiceResponses);

		return Response.success(withPetServiceResponses);
	}
}
