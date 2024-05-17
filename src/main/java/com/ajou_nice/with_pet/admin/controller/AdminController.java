package com.ajou_nice.with_pet.admin.controller;


import com.ajou_nice.with_pet.admin.model.dto.accept_applicant.AdminAcceptApplicantResponse;
import com.ajou_nice.with_pet.admin.model.dto.add_critical.AddCriticalServiceRequest;
import com.ajou_nice.with_pet.admin.model.dto.add_critical.AddWithPetServiceRequest;
import com.ajou_nice.with_pet.admin.model.dto.get_applicant.AdminGetApplicantBasicResponse;
import com.ajou_nice.with_pet.admin.model.dto.get_applicant.AdminGetApplicantDetailResponse;
import com.ajou_nice.with_pet.admin.model.dto.refuse_applicant.AdminRefuseApplicantResponse;
import com.ajou_nice.with_pet.admin.model.dto.update_critical.UpdateCriticalServiceRequest;
import com.ajou_nice.with_pet.admin.model.dto.update_critical.UpdateCriticalServiceResponse;
import com.ajou_nice.with_pet.admin.model.dto.update_service.UpdateWithPetServiceRequest;
import com.ajou_nice.with_pet.admin.service.AdminApplicantService;
import com.ajou_nice.with_pet.admin.service.AdminService;
import com.ajou_nice.with_pet.critical_service.model.dto.CriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterBasicResponse;
import com.ajou_nice.with_pet.withpet_service.model.dto.WithPetServiceResponse;
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
    private final AdminApplicantService adminApplicantService;

    @PatchMapping("/accept-applicants/{userId}")
    @ApiOperation(value = "관리자의 펫시터 지원자 수락")
    public Response<AdminAcceptApplicantResponse> acceptApplicant(@ApiIgnore Authentication authentication, @PathVariable("userId") Long userId) {

        AdminAcceptApplicantResponse adminAcceptApplicantResponse = adminApplicantService.acceptApplicant(authentication.getName(), userId);

        return Response.success(adminAcceptApplicantResponse);
    }

    @PatchMapping("/refuse-applicant/{userId}")
	@ApiOperation(value = "관리자의 펫시터 지원자 거절")
	public Response<AdminRefuseApplicantResponse> refuseApplicant(@ApiIgnore Authentication authentication, @PathVariable("userId") Long userId){

		AdminRefuseApplicantResponse adminRefuseApplicantResponse = adminApplicantService.refuseApplicant(authentication.getName(), userId);

		return Response.success(adminRefuseApplicantResponse);
	}

    @GetMapping("/applicants")
    @ApiOperation(value = "펫시터 지원자 리스트 조회")
    public Response<List<AdminGetApplicantBasicResponse>> getApplicants(@ApiIgnore Authentication authentication) {
        List<AdminGetApplicantBasicResponse> applicantList = adminApplicantService.getApplicants(
                authentication.getName());
        return Response.success(applicantList);
    }

    @GetMapping("applicants/{userId}")
    @ApiOperation(value = "펫시터 지원자 정보 상세 확인")
    public Response<AdminGetApplicantDetailResponse> getApplicantDetailInfo(@ApiIgnore Authentication authentication, @PathVariable("userId") Long userId) {

        AdminGetApplicantDetailResponse applicantDetailResponse = adminApplicantService.getApplicantDetailInfo(authentication.getName(), userId);

        return Response.success(applicantDetailResponse);
    }

    @GetMapping("/pet-sitters")
    @ApiOperation(value = "관리자의 펫시터 리스트 조회")
    public Response<List<PetSitterBasicResponse>> showPetSitters(@ApiIgnore Authentication authentication){
        List<PetSitterBasicResponse> petSitterBasicResponses = adminService.showPetSitters(authentication.getName());
        return Response.success(petSitterBasicResponses);
    }

    @PostMapping("/critical-service")
    @ApiOperation(value = "관리자의 필수 서비스 추가")
    public Response<CriticalServiceResponse> addCriticalService(@ApiIgnore Authentication authentication, @RequestBody @Valid
    AddCriticalServiceRequest addCriticalServiceRequest) {
        CriticalServiceResponse criticalServiceResponse = adminService.addCriticalService(
                authentication.getName(), addCriticalServiceRequest);
        return Response.success(criticalServiceResponse);
    }

    @PutMapping("/critical-service/{serviceId}")
    @ApiOperation(value = "관리자의 필수 서비스 수정")
    public Response<UpdateCriticalServiceResponse> updateCriticalService(@ApiIgnore Authentication authentication, @PathVariable("serviceId") Long serviceId, @RequestBody @Valid UpdateCriticalServiceRequest updateCriticalServiceRequest){
        UpdateCriticalServiceResponse updateCriticalServiceResponse = adminService.updateCriticalService(authentication.getName(), serviceId, updateCriticalServiceRequest);

        return Response.success(updateCriticalServiceResponse);
    }

    @PostMapping("/service")
    @ApiOperation(value = "관리자의 위드펫 서비스 추가")
    public Response<WithPetServiceResponse> addWithPetService(@ApiIgnore Authentication authentication, @RequestBody @Valid
    AddWithPetServiceRequest addWithPetServiceRequest) {
        WithPetServiceResponse withPetServiceResponse = adminService.addWithPetService(
                authentication.getName(), addWithPetServiceRequest);
        return Response.success(withPetServiceResponse);
    }

	@PutMapping("/service/{serviceId}")
	@ApiOperation(value = "관리자의 위드펫 서비스 수정")
	public Response<WithPetServiceResponse> updateWithPetService(@ApiIgnore Authentication authentication, @PathVariable("serviceId") Long serviceId, @RequestBody @Valid UpdateWithPetServiceRequest updateWithPetServiceRequest){

		WithPetServiceResponse withPetServiceResponse = adminService.updateWithPetService(
				authentication.getName(),serviceId,updateWithPetServiceRequest);

		return Response.success(withPetServiceResponse);
	}

    // 펫시터가 등록한 위드펫 서비스가 있을 경우 삭제 못 하도록 설정해야할 듯 (나중)
	@DeleteMapping  ("/service/{serviceId}")
	@ApiOperation(value = "관리자의 위드펫 서비스 삭제")
	public Response<List<WithPetServiceResponse>> deleteWithPetService(@ApiIgnore Authentication authentication, @PathVariable("serviceId") Long serviceId){

		List<WithPetServiceResponse> withPetServiceResponses = adminService.deleteWithPetService(authentication.getName(), serviceId);

		return Response.success(withPetServiceResponses);
	}
}
