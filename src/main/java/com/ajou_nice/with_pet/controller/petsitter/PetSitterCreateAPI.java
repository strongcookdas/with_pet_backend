package com.ajou_nice.with_pet.controller.petsitter;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.dto.petsitter.PetSitterCreateRequest;
import com.ajou_nice.with_pet.dto.petsitter.PetSitterCreateResponse;
import com.ajou_nice.with_pet.service.petsitter.PetSitterCreateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/pet-sitters")
@Api(tags = "PetSitter api")
public class PetSitterCreateAPI {

    private final PetSitterCreateService service;

    /**
     * 펫시터 정보 등록
     **/
    @PostMapping("/info")
    @ApiOperation(value = "펫시터의 펫시터정보 등록")
    public Response<PetSitterCreateResponse> registerPetSitterInfo(
            @ApiIgnore Authentication authentication,
            @RequestBody @Valid PetSitterCreateRequest petSitterCreateRequest) {
        PetSitterCreateResponse petSitterCreateResponse = service.registerPetSitterInfo(
                petSitterCreateRequest,
                authentication.getName());

        return Response.success(petSitterCreateResponse);
    }
}
