package com.ajou_nice.with_pet.controller.petsitter;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse.PetSitterModifyInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest;
import com.ajou_nice.with_pet.service.petsitter.PetSitterCreateService;
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
@Api(tags = "PetSitter api")
public class PetSitterCreateAPI {

    PetSitterCreateService service;

    /**
     * 펫시터 정보 등록
     **/
    @PostMapping("/api/v1/petsitter/register-myinfo")
    @ApiOperation(value = "펫시터의 펫시터정보 등록")
    public Response<PetSitterModifyInfoResponse> registerPetSitterInfo(
            @ApiIgnore Authentication authentication,
            @RequestBody @Valid PetSitterRequest.PetSitterInfoRequest petSitterInfoRequest) {
        log.info("=============== petSitter register info request : {} ================",
                petSitterInfoRequest);

        PetSitterModifyInfoResponse modifyInfoResponse = service.registerPetSitterInfo(
                petSitterInfoRequest,
                authentication.getName());

        log.info("=============== petSitter register info response : {} ================",
                modifyInfoResponse);
        return Response.success(modifyInfoResponse);
    }
}
