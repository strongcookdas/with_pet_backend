package com.ajou_nice.with_pet.controller.applicant;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.dto.applicant.PetsitterApplicationRequest;
import com.ajou_nice.with_pet.dto.applicant.PetsitterApplicationResponse;
import com.ajou_nice.with_pet.service.applicant.ApplicantCreateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "Applicant Create API")
public class ApplicantCreateAPI {

    private final ApplicantCreateService service;

    @PostMapping("/api/v2/applicants")
    @ApiOperation(value = "유저의 펫시터 지원")
    public Response<PetsitterApplicationResponse> registerApplicant(@ApiIgnore Authentication authentication
            , @RequestBody @Valid PetsitterApplicationRequest petsitterApplicationRequest){

        PetsitterApplicationResponse petsitterApplicationResponse = service.registerApplicant(
                petsitterApplicationRequest,
                authentication.getName());

        return Response.success(petsitterApplicationResponse);
    }
}
