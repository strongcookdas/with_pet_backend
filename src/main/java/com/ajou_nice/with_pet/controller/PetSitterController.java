package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse.PetSitterModifyInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterMainResponse;
import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterCriticalServicesRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHashTagsRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHousesRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterIntroRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterWithPetServicesRequest;
import com.ajou_nice.with_pet.service.PetSitterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "PetSitter api")
public class PetSitterController {

    private final PetSitterService petSitterService;

    // 펫시터에서 필요한 api //

    // 펫시터 상세정보 조회 api //
    @GetMapping("api/v1/petsitter/{petsitterId}")
    @ApiOperation(value = "사용자의 펫시터 상세 정보 조회")
    public Response<PetSitterDetailInfoResponse> showPetSitterInfo(
            @PathVariable("petsitterId") Long petSitterId) {

        PetSitterDetailInfoResponse petSitterDetailInfoResponse = petSitterService.showPetSitterDetailInfo(
                petSitterId);

        return Response.success(petSitterDetailInfoResponse);
    }

    // 펫시터 현재 자신 정보 조회 //
    @GetMapping("/api/v1/petsitter/show-myinfo")
    @ApiOperation(value = "펫시터의 자신 정보 조회")
    public Response<PetSitterModifyInfoResponse> showPetSitterSelfInfo(
            @ApiIgnore Authentication authentication) {

        PetSitterModifyInfoResponse modifyInfoResponse = petSitterService.showMyInfo(
                authentication.getName());

        log.info("=============== petsitter's info : {} ================", modifyInfoResponse);

        return Response.success(modifyInfoResponse);
    }

    // 펫시터 my Info 등록  //
    @PostMapping("/api/v1/petsitter/register-myinfo")
    @ApiOperation(value = "펫시터의 펫시터정보 등록")
    public Response<PetSitterModifyInfoResponse> registerPetSitterInfo(
            @ApiIgnore Authentication authentication,
            @RequestBody @Valid PetSitterRequest.PetSitterInfoRequest petSitterInfoRequest) {
        log.info("=============== petSitter register info request : {} ================",
                petSitterInfoRequest);

        PetSitterModifyInfoResponse modifyInfoResponse = petSitterService.registerPetSitterInfo(
                petSitterInfoRequest,
                authentication.getName());

        log.info("=============== petSitter register info response : {} ================",
                modifyInfoResponse);
        return Response.success(modifyInfoResponse);
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
            @PageableDefault(size = 20, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String dogSize,
            @RequestParam(required = false) List<String> service,
            @RequestParam(required = false) String address) {
        log.info("=================== 필터링 {},{},{} ======================",dogSize,service,address);
        Page<PetSitterMainResponse> petSitterMainResponses = petSitterService.getPetSitters(
                pageable, dogSize, service, address);
        return Response.success(petSitterMainResponses);
    }
}
