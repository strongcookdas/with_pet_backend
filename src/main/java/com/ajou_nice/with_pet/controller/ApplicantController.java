package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.auth.UserSimpleRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoResponse;
import com.ajou_nice.with_pet.service.ApplicantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "Applicant api")
public class ApplicantController {

	private final ApplicantService applicantService;
	@PostMapping("/api/v1/applicate-petsitter")
	@ApiOperation(value = "유저의 펫시터 지원")
	public Response<ApplicantInfoResponse> registerApplicants(@Valid @RequestBody UserSimpleRequest userSimpleRequest
			,@RequestBody @Valid ApplicantInfoRequest applicantInfoRequest){
		log.info("===================applicant register Request : {} ===================", applicantInfoRequest);
		ApplicantInfoResponse applicantInfoResponse = applicantService.registerApplicant(applicantInfoRequest,
				userSimpleRequest.getUserId());
		log.info("===================applicant register Response : {} ===================", applicantInfoResponse);

		return Response.success(applicantInfoResponse);
	}

	/*
	@GetMapping("/api/v1/applicants/{userId}")
	public Response<>

	@Data
	static class CreateApplicantRequest{
		private String name;
	}
	*/



}
