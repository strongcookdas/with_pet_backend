package com.ajou_nice.with_pet.dog.controller;

import com.ajou_nice.with_pet.dog.model.dto.DogInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogListInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogSocializationRequest;
import com.ajou_nice.with_pet.dog.model.dto.add.DogRegisterRequest;
import com.ajou_nice.with_pet.dog.model.dto.add.DogRegisterResponse;
import com.ajou_nice.with_pet.dog.model.dto.delete.DogDeleteResponse;
import com.ajou_nice.with_pet.dog.model.dto.get.DogGetInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.get.DogGetInfosResponse;
import com.ajou_nice.with_pet.dog.model.dto.update.DogUpdateInfoRequest;
import com.ajou_nice.with_pet.dog.model.dto.update.DogUpdateInfoResponse;
import com.ajou_nice.with_pet.dog.service.DogService;
import com.ajou_nice.with_pet.domain.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("api/v2/dogs")
@RequiredArgsConstructor
@Api(tags = "Dog API")
@Slf4j
public class DogController {

    private final DogService dogService;

    @PostMapping("/{partyId}")
    @ApiOperation(value = "반려견 등록")
    public Response<DogRegisterResponse> registerDog(@ApiIgnore Authentication authentication, @PathVariable Long partyId, @RequestBody DogRegisterRequest dogRegisterRequest) {
        DogRegisterResponse dogRegisterResponse = dogService.registerDog(authentication.getName(), partyId, dogRegisterRequest);
        return Response.success(dogRegisterResponse);
    }

    @GetMapping("/{dogId}")
    @ApiOperation(value = "반려견 상세정보 조회")
    public Response<DogGetInfoResponse> getDogInfo(@ApiIgnore Authentication authentication, @PathVariable Long dogId) {
        DogGetInfoResponse dogInfoResponse = dogService.getDogInfo(authentication.getName(), dogId);
        return Response.success(dogInfoResponse);
    }

    @PutMapping("/{dogId}")
    @ApiOperation(value = "반려견 상세정보 수정")
    public Response<DogUpdateInfoResponse> updateDogInfo(@ApiIgnore Authentication authentication, @PathVariable Long dogId, @RequestBody DogUpdateInfoRequest dogUpdateInfoRequest) {
        DogUpdateInfoResponse dogInfoResponse = dogService.updateDog(authentication.getName(), dogId, dogUpdateInfoRequest);
        return Response.success(dogInfoResponse);
    }

    @GetMapping
    @ApiOperation(value = "반려견 상세정보 목록")
    public Response<List<DogGetInfosResponse>> getDogInfos(@ApiIgnore Authentication authentication) {
        List<DogGetInfosResponse> dogInfoResponses = dogService.getDogInfos(authentication.getName());
        return Response.success(dogInfoResponses);
    }

    @DeleteMapping("/{dogId}")
    @ApiOperation(value = "반려견 삭제")
    public Response<DogDeleteResponse> deleteDog(@ApiIgnore Authentication authentication, @PathVariable Long dogId) {
        DogDeleteResponse dogDeleteResponse = dogService.deleteDog(authentication.getName(), dogId);
        return Response.success(dogDeleteResponse);
    }

    @PutMapping("/temperature/{dogId}")
    @ApiOperation(value = "반려견 사회화 정도 등록")
    public Response<DogInfoResponse> modifyDogSocialization(
            @ApiIgnore Authentication authentication,
            @PathVariable Long dogId,
            @RequestBody DogSocializationRequest dogSocializationRequest) {

        log.info(
                "---------------------dog Modify socialization Request : {}--------------------------",
                dogSocializationRequest);

        DogInfoResponse dogInfoResponse = dogService.modifyDogSocialization(
                authentication.getName(), dogId, dogSocializationRequest);

        log.info(
                "---------------------dog Modify socialization Response : {}--------------------------",
                dogInfoResponse);

        return Response.success(dogInfoResponse);
    }

    @GetMapping("/reservation-dogs")
    @ApiOperation(value = "예약 페이지 반려견 리스트 조회")
    public Response<List<DogListInfoResponse>> getDogListInfoResponses(
            @ApiIgnore Authentication authentication,
            Long petSitterId) {
        List<DogListInfoResponse> list = dogService.getDogListInfoResponse(authentication.getName(),
                petSitterId);
        return Response.success(list);
    }
}
