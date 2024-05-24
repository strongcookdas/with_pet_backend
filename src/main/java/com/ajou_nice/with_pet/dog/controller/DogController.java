package com.ajou_nice.with_pet.dog.controller;

import com.ajou_nice.with_pet.dog.model.dto.add.DogRegisterRequest;
import com.ajou_nice.with_pet.dog.model.dto.add.DogRegisterResponse;
import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.dog.model.dto.DogInfoRequest;
import com.ajou_nice.with_pet.dog.model.dto.DogInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogListInfoResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogSocializationRequest;
import com.ajou_nice.with_pet.dog.service.DogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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
    public Response<DogInfoResponse> getDogInfo(@ApiIgnore Authentication authentication,
            @PathVariable Long dogId) {
        DogInfoResponse dogInfoResponse = dogService.getDogInfo(dogId, authentication.getName());
        log.info("---------------------dog DogInfo Response : {}--------------------------",
                dogInfoResponse);
        return Response.success(dogInfoResponse);
    }

    @PutMapping("/{dogId}")
    @ApiOperation(value = "반려견 상세정보 수정")
    public Response<DogInfoResponse> modifyDogInfo(@ApiIgnore Authentication authentication,
            @PathVariable Long dogId,
            @RequestBody DogInfoRequest dogInfoRequest) {
        log.info("---------------------dog Modify Request : {}--------------------------",
                dogInfoRequest);
        DogInfoResponse dogInfoResponse = dogService.modifyDogInfo(dogId, dogInfoRequest,
                authentication.getName());
        log.info("---------------------dog Modify Request : {}--------------------------",
                dogInfoResponse);
        return Response.success(dogInfoResponse);
    }

    @GetMapping
    @ApiOperation(value = "반려견 상세정보 목록")
    public Response<List<DogInfoResponse>> getDogInfos(@ApiIgnore Authentication authentication) {

        log.info(
                "================================= 반려견 상세정보 목록 시작 =================================");
        List<DogInfoResponse> dogInfoResponses = dogService.getDogInfos(authentication.getName());
        log.info(
                "================================= 반려견 상세정보 목록 끝 =================================");
        return Response.success(dogInfoResponses);
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

    @DeleteMapping("/{dogId}")
    @ApiOperation(value = "반려견 삭제")
    public Response<Boolean> deleteDog(@ApiIgnore Authentication authentication, @PathVariable Long dogId){
        return Response.success(dogService.deleteDog(authentication.getName(),dogId));
    }
}
