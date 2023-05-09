package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantRequest;
import com.ajou_nice.with_pet.domain.dto.admin.AdminAcceptApplicantResponse;
import com.ajou_nice.with_pet.domain.dto.admin.AdminApplicantResponse;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantBasicInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoResponse;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "Administrator api")
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/api/v1/admin/accept-petsitter")
	@ApiOperation(value = "관리자의 펫시터 지원자 수락")
	public Response<AdminAcceptApplicantResponse> acceptApplicant(@RequestBody @Valid AdminApplicantRequest adminApplicantRequest){

		log.info("=============== accept petsitter info : {} ==================",
				adminApplicantRequest);
		AdminAcceptApplicantResponse adminAcceptApplicantResponse = adminService.createPetsitter(
				adminApplicantRequest);

		log.info("=============== accepted petsitter info : {} =================", adminAcceptApplicantResponse);
		return Response.success(adminAcceptApplicantResponse);
	}

	@GetMapping("/api/v1/show-applicants")
	@ApiOperation(value = "펫시터 지원자 리스트 전체 확인")
	public Response<List<ApplicantBasicInfoResponse>> showApplicants(){

		List<ApplicantBasicInfoResponse> applicantList = adminService.showApplicants();
		log.info("===================applicant Info List Response : {} ==================", applicantList);
		return Response.success(applicantList);
	}

	@GetMapping("/api/v1/show-applicant/{petSitterId}")
	@ApiOperation(value = "펫시터 지원자 정보 상세 확인")
	public Response<ApplicantInfoResponse> getApplicant(@PathVariable("petSitterId")Long petSitterId){

		ApplicantInfoResponse applicantInfoResponse = adminService.getApplicantInfo(petSitterId);

		return Response.success(applicantInfoResponse);
	}

	@PostMapping("/api/v1/admin/refuse-applicant")
	@ApiOperation(value = "관리자의 펫시터 지원자 거절")
	public Response<AdminApplicantResponse> refuseApplicant(@RequestBody @Valid AdminApplicantRequest adminApplicantRequest){
		log.info("=============== refuse petsitter info : {} ==================",adminApplicantRequest);

		AdminApplicantResponse adminApplicantResponse = adminService.refuseApplicant(adminApplicantRequest);

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

	@PostMapping("/api/v1/admin/add-service")
	@ApiOperation(value = "관리자의 위드펫 서비스 추가")
	public Response<WithPetServiceResponse> addWithPetService(@RequestBody @Valid
			WithPetServiceRequest withPetServiceRequest){

		log.info("=============== request create withPetService info : {} ==================",withPetServiceRequest);
		WithPetServiceResponse withPetServiceResponse = adminService.addWithPetService(withPetServiceRequest);

		log.info("=============== response create withPetService info : {} ==================",withPetServiceResponse);

		return Response.success(withPetServiceResponse);
	}

	@PutMapping("/api/v1/admin/service")
	@ApiOperation(value = "관리자의 위드펫 서비스 수정")
	public Response<WithPetServiceResponse> updateWithPetService(@RequestBody @Valid
			WithPetServiceModifyRequest withPetServiceModifyRequest){

		log.info("=============== request update withPetService info : {} ==================",withPetServiceModifyRequest);

		WithPetServiceResponse withPetServiceResponse = adminService.updateWithPetService(withPetServiceModifyRequest);

		log.info("=============== response update withPetService info : {} ==================",withPetServiceResponse);

		return Response.success(withPetServiceResponse);
	}

	@DeleteMapping("/api/v1/admin/service")
	@ApiOperation(value = "관리자의 위드펫 서비스 삭제")
	public Response<List<WithPetServiceResponse>> deleteWithPetService(@RequestBody @Valid
			WithPetServiceModifyRequest withPetServiceModifyRequest){

		log.info("=============== request delete withPetService info : {} ==================",withPetServiceModifyRequest);

		List<WithPetServiceResponse> withPetServiceResponses = adminService.deleteWithPetService(withPetServiceModifyRequest);

		log.info("=============== response deleted withPetService List : {} ==================",withPetServiceResponses);

		return Response.success(withPetServiceResponses);
	}
}
