package com.ajou_nice.with_pet.controller.applicant;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.dto.applicant.PetsitterApplicationRequest;
import com.ajou_nice.with_pet.dto.applicant.PetsitterApplicationResponse;
import com.ajou_nice.with_pet.service.applicant.ApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/api/v2/applications")
@RequiredArgsConstructor
@Api(tags = "Applicant API")
public class ApplicantController {

	private final ApplicationService applicationService;

	@PostMapping
	@ApiOperation(value = "유저의 펫시터 지원")
	public Response<PetsitterApplicationResponse> registerApplicant(@ApiIgnore Authentication authentication
			, @RequestBody @Valid PetsitterApplicationRequest petsitterApplicationRequest){
		PetsitterApplicationResponse petsitterApplicationResponse = applicationService.applyPetsitter(
				petsitterApplicationRequest,
				authentication.getName());
		return Response.success(petsitterApplicationResponse);
	}

//	@GetMapping("/api/v1/users/show-applicateInfo")
//	@ApiOperation(value = "유저의 자신 지원정보 확인")
//	public Response<PetsitterApplicationResponse> showApplicateInfo(@ApiIgnore Authentication authentication){
//
//		PetsitterApplicationResponse petsitterApplicationResponse = applicantService.showApplicateInfo(
//				authentication.getName());
//		log.info("===================applicant register Response : {} ===================",
//				petsitterApplicationResponse);
//		return Response.success(petsitterApplicationResponse);
//	}
//
//	@PutMapping("/api/v1/users/update-applicateInfo")
//	@ApiOperation(value = "유저의 자신 지원정보 수정")
//	public Response<PetsitterApplicationResponse> updateApplicateInfo(@ApiIgnore Authentication authentication,
//																	  @RequestBody @Valid ApplicantModifyRequest applicantModifyRequest){
//
//		log.info("===================modify applicate info : {} ==================", applicantModifyRequest);
//
//		PetsitterApplicationResponse petsitterApplicationResponse = applicantService.modifyApplicateInfo(
//				authentication.getName(), applicantModifyRequest);
//
//		return Response.success(petsitterApplicationResponse);
//	}
}
