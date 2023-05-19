package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.dog.DogInfoRequest;
import com.ajou_nice.with_pet.domain.dto.dog.DogInfoResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogListInfoResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogSocializationRequest;
import com.ajou_nice.with_pet.service.DogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v1/dogs")
@RequiredArgsConstructor
@Api(tags = "Dog API")
@Slf4j
public class DogController {

    private final DogService dogService;

    @PostMapping("/register-dog")
    @ApiOperation(value = "반려견 등록")
    public Response<DogInfoResponse> registerDog(@ApiIgnore Authentication authentication,
            @RequestBody DogInfoRequest dogInfoRequest) {
        log.info("---------------------dog Register Request : {}--------------------------",
                dogInfoRequest);
        DogInfoResponse dogInfoResponse = dogService.registerDog(dogInfoRequest,
                authentication.getName());
        log.info("---------------------dog Register Response : {}--------------------------",
                dogInfoResponse);
        return Response.success(dogInfoResponse);
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
    public Response<Page<DogInfoResponse>> getDogInfos(
            @ApiIgnore @PageableDefault(size = 10, sort = "createdAt", direction = Direction.ASC)
            Pageable pageable, @ApiIgnore Authentication authentication) {

        Page<DogInfoResponse> dogInfoResponses = dogService.getDogInfos(pageable,
                authentication.getName());
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
    public Response<List<DogListInfoResponse>> getDogListInfoResponses(@ApiIgnore Authentication authentication,
            Long petSitterId) {
        List<DogListInfoResponse> list = dogService.getDogListInfoResponse(authentication.getName(), petSitterId);
        return Response.success(list);
    }
}
