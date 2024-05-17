package com.ajou_nice.with_pet.petsitter.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.petsitter.model.constant.PetSitterResponseMessages;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterMainResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterRequest.PetSitterCriticalServicesRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterRequest.PetSitterIntroRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.get_detail_info.PetSitterDetailInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.get_detail_info.PetSitterDetailInfoResponse.PetSitterMyInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.update_house.PetSitterUpdateHousesRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.register_info.PetSitterRegisterInfoRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.register_info.PetSitterRegisterInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.update_hash_tag.PetSitterHashTagsRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.update_service.PetSitterUpdateWithPetServicesRequest;
import com.ajou_nice.with_pet.petsitter.service.PetSitterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/pet-sitters")
@Api(tags = "PetSitter API")
public class PetSitterController {

    private final PetSitterService petSitterService;

    @GetMapping("/{petSitterId}")
    @ApiOperation(value = "사용자의 펫시터 상세 정보 조회")
    public Response<PetSitterDetailInfoResponse> getPetSitterInfo(@PathVariable("petSitterId") Long petSitterId) {
        PetSitterDetailInfoResponse petSitterDetailInfoResponse = petSitterService.getPetSitterDetailInfo(petSitterId);
        return Response.success(petSitterDetailInfoResponse);
    }

    @GetMapping("/my-info")
    @ApiOperation(value = "펫시터의 자신 정보 조회")
    public Response<PetSitterMyInfoResponse> getPetSitterSelfInfo(@ApiIgnore Authentication authentication) {
        PetSitterMyInfoResponse myInfoResponse = petSitterService.getMyInfo(authentication.getName());
        return Response.success(myInfoResponse);
    }

    @PostMapping("/my-info")
    @ApiOperation(value = "펫시터의 정보 등록")
    public Response<PetSitterRegisterInfoResponse> registerPetSitterInfo(@ApiIgnore Authentication authentication, @RequestBody @Valid PetSitterRegisterInfoRequest petSitterRegisterInfoRequest) {
        PetSitterRegisterInfoResponse registerInfoResponse = petSitterService.registerPetSitterInfo(authentication.getName(), petSitterRegisterInfoRequest);

        return Response.success(registerInfoResponse);
    }

    @PutMapping("/houses")
    @ApiOperation(value = "펫시터 펫시터집 사진 수정")
    public Response updatePetSitterHouses(@ApiIgnore Authentication authentication, @RequestBody @Valid PetSitterUpdateHousesRequest petSitterHousesRequest) {
        petSitterService.updatePetSitterHouses(authentication.getName(), petSitterHousesRequest);
        return Response.success(PetSitterResponseMessages.HOUSE_UPDATE.getMessage());
    }

    @PutMapping("/hashtags")
    @ApiOperation(value = "펫시터 해시태그 수정")
    public Response updatePetSitterHashTags(@ApiIgnore Authentication authentication, @RequestBody @Valid PetSitterHashTagsRequest petSitterHashTagsRequest) {
        petSitterService.updateHashTags(authentication.getName(), petSitterHashTagsRequest);
        return Response.success(PetSitterResponseMessages.HASHTAG_UPDATE.getMessage());
    }

    @PutMapping("/services")
    @ApiOperation(value = "펫시터 위드펫 서비스 수정")
    public Response modifyPetSitterService(@ApiIgnore Authentication authentication, @RequestBody @Valid PetSitterUpdateWithPetServicesRequest withPetServicesRequest) {
        petSitterService.updatePetSitterServices(authentication.getName(), withPetServicesRequest);
        return Response.success(PetSitterResponseMessages.WITH_PET_SERVICE_UPDATE.getMessage());
    }

    // 펫시터 critical withpetService 정보 수정 //
    @PutMapping("/api/v1/petsitter/update-criticalservice")
    @ApiOperation(value = "펫시터 필수 위드펫 서비스 수정")
    public Response modifyCriticalService(
            @RequestBody @Valid PetSitterCriticalServicesRequest criticalServicesRequest,
            @ApiIgnore Authentication authentication) {

        petSitterService.updateCriticalService(criticalServicesRequest, authentication.getName());
        return Response.success("수정이 완료되었습니다.");
    }

    // 펫시터 introduction 정보 수정 //
    @PutMapping("/api/v1/petsitter/update-intro")
    @ApiOperation(value = "펫시터 introduction 정보 수정")
    public Response modifyIntro(@RequestBody PetSitterIntroRequest petSitterIntroRequest,
                                @ApiIgnore Authentication authentication) {

        petSitterService.updatePetSitterIntro(petSitterIntroRequest, authentication.getName());

        return Response.success("수정이 완료되었습니다.");
    }

    // 메인페이지 펫시터 조회 //
    @GetMapping("/api/v1/show-petsitter")
    @ApiOperation(value = "메인페이지 펫시터들 조회")
    public Response<Page<PetSitterMainResponse>> showPetSitters(
            @PageableDefault(size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String dogSize,
            @RequestParam(required = false) List<String> service,
            @RequestParam(required = false) String address) {
        log.info("=================== 필터링 {},{},{} ======================", dogSize, service, address);
        Page<PetSitterMainResponse> petSitterMainResponses = petSitterService.getPetSitters(
                pageable, dogSize, service, address);
        return Response.success(petSitterMainResponses);
    }
}