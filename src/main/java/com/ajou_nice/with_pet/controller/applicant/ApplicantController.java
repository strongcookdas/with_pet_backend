package com.ajou_nice.with_pet.controller.applicant;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest.ApplicantModifyRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoResponse;
import com.ajou_nice.with_pet.service.applicant.ApplicantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "Applicant api")
public class ApplicantController {

	private final ApplicantService applicantService;

	@PostMapping("/api/v2/applicants")
	@ApiOperation(value = "유저의 펫시터 지원")
	public Response<ApplicantInfoResponse> registerApplicant(@ApiIgnore Authentication authentication
			,@RequestBody @Valid ApplicantInfoRequest applicantInfoRequest){

		ApplicantInfoResponse applicantInfoResponse = applicantService.registerApplicant(applicantInfoRequest,
				authentication.getName());

		return Response.success(applicantInfoResponse);
	}

	@GetMapping("/api/v1/users/show-applicateInfo")
	@ApiOperation(value = "유저의 자신 지원정보 확인")
	public Response<ApplicantInfoResponse> showApplicateInfo(@ApiIgnore Authentication authentication){

		ApplicantInfoResponse applicantInfoResponse = applicantService.showApplicateInfo(
				authentication.getName());
		log.info("===================applicant register Response : {} ===================", applicantInfoResponse);
		return Response.success(applicantInfoResponse);
	}

	@PutMapping("/api/v1/users/update-applicateInfo")
	@ApiOperation(value = "유저의 자신 지원정보 수정")
	public Response<ApplicantInfoResponse> updateApplicateInfo(@ApiIgnore Authentication authentication,
			@RequestBody @Valid ApplicantModifyRequest applicantModifyRequest){

		log.info("===================modify applicate info : {} ==================", applicantModifyRequest);

		ApplicantInfoResponse applicantInfoResponse = applicantService.modifyApplicateInfo(
				authentication.getName(), applicantModifyRequest);

		return Response.success(applicantInfoResponse);
	}
}
