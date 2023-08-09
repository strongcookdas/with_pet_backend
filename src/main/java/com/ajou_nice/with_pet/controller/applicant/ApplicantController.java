package com.ajou_nice.with_pet.controller.applicant;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateRequest.ApplicantModifyRequest;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateResponse;
import com.ajou_nice.with_pet.service.applicant.ApplicantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/api/v1/users/show-applicateInfo")
	@ApiOperation(value = "유저의 자신 지원정보 확인")
	public Response<ApplicantCreateResponse> showApplicateInfo(@ApiIgnore Authentication authentication){

		ApplicantCreateResponse applicantCreateResponse = applicantService.showApplicateInfo(
				authentication.getName());
		log.info("===================applicant register Response : {} ===================",
				applicantCreateResponse);
		return Response.success(applicantCreateResponse);
	}

	@PutMapping("/api/v1/users/update-applicateInfo")
	@ApiOperation(value = "유저의 자신 지원정보 수정")
	public Response<ApplicantCreateResponse> updateApplicateInfo(@ApiIgnore Authentication authentication,
			@RequestBody @Valid ApplicantModifyRequest applicantModifyRequest){

		log.info("===================modify applicate info : {} ==================", applicantModifyRequest);

		ApplicantCreateResponse applicantCreateResponse = applicantService.modifyApplicateInfo(
				authentication.getName(), applicantModifyRequest);

		return Response.success(applicantCreateResponse);
	}
}
