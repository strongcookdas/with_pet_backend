package com.ajou_nice.with_pet.petsitter.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.petsitter.model.dto.*;
import com.ajou_nice.with_pet.petsitter.model.dto.detail.PetSitterDetailInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.detail.PetSitterDetailInfoResponse.PetSitterMyInfoResponse;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterRequest.*;
import com.ajou_nice.with_pet.petsitter.model.dto.register_info.PetSitterRegisterInfoRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.register_info.PetSitterRegisterInfoResponse;
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

    // 펫시터 house 수정 //
    @PutMapping("/api/v1/petsitter/update-houses")
    @ApiOperation(value = "펫시터 펫시터집 사진 수정")
    public Response modifyPetSitterHouses(
            @RequestBody @Valid PetSitterHousesRequest petSitterHousesRequest,
            @ApiIgnore Authentication authentication) {

        petSitterService.updateHouseInfo(petSitterHousesRequest, authentication.getName());

        return Response.success("수정이 완료되었습니다.");
    }

    // 펫시터 HashTag 수정 //
    @PutMapping("/api/v1/petsitter/update-hashtags")
    @ApiOperation(value = "펫시터 해시태그 수정")
    public Response modifyPetSitterHashTags(
            @RequestBody @Valid PetSitterHashTagsRequest petSitterHashTagsRequest,
            @ApiIgnore Authentication authentication) {

        petSitterService.updateHashTagInfo(petSitterHashTagsRequest, authentication.getName());

        return Response.success("수정이 완료되었습니다.");
    }

    //펫시터 withPetService 정보 수정 //
    @PutMapping("/api/v1/petsitter/update-service")
    @ApiOperation(value = "펫시터 위드펫 서비스 수정")
    public Response modifyPetSitterService(
            @RequestBody @Valid PetSitterWithPetServicesRequest withPetServicesRequest,
            @ApiIgnore Authentication authentication) {

        petSitterService.updatePetSitterService(withPetServicesRequest, authentication.getName());

        return Response.success("수정이 완료되었습니다.");
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
