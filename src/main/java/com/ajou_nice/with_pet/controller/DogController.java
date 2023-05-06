package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.dog.DogInfoRequest;
import com.ajou_nice.with_pet.domain.dto.dog.DogInfoResponse;
import com.ajou_nice.with_pet.service.DogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/dogs")
@RequiredArgsConstructor
@Api(tags = "Dog API")
@Slf4j
public class DogController {

    private final DogService dogService;

    @PostMapping("/register-dog")
    @ApiOperation(value = "반려견 등록")
    public Response<DogInfoResponse> registerDog(@RequestBody DogInfoRequest dogInfoRequest) {
        log.info("---------------------dog Register Request : {}--------------------------",
                dogInfoRequest);
        DogInfoResponse dogInfoResponse = dogService.registerDog(dogInfoRequest);
        log.info("---------------------dog Register Response : {}--------------------------",
                dogInfoResponse);
        return Response.success(dogInfoResponse);
    }

    @GetMapping("/{dogId}")
    @ApiOperation(value = "반려견 상세정보 조회")
    public Response<DogInfoResponse> getDogInfo(@PathVariable Long dogId) {
        DogInfoResponse dogInfoResponse = dogService.getDogInfo(dogId);
        log.info("---------------------dog DogInfo Response : {}--------------------------",
                dogInfoResponse);
        return Response.success(dogInfoResponse);
    }

    @PutMapping("/{dogId}")
    @ApiOperation(value = "반려견 상세정보 수정")
    public Response<DogInfoResponse> modifyDogInfo(@PathVariable Long dogId,
            @RequestBody DogInfoRequest dogInfoRequest) {
        log.info("---------------------dog Modify Request : {}--------------------------",
                dogInfoRequest);
        DogInfoResponse dogInfoResponse = dogService.modifyDogInfo(dogId, dogInfoRequest);
        log.info("---------------------dog Modify Request : {}--------------------------",
                dogInfoResponse);
        return Response.success(dogInfoResponse);
    }

    @GetMapping
    @ApiOperation(value = "반려견 상세정보 목록")
    public Response<List<DogInfoResponse>> getDogInfos() {
        List<DogInfoResponse> dogInfoResponses = dogService.getDogInfos();
        return Response.success(dogInfoResponses);
    }
}
