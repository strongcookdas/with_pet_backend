package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.diary.model.dto.DiaryModifyRequest;
import com.ajou_nice.with_pet.diary.model.dto.DiaryRequest;
import com.ajou_nice.with_pet.diary.model.dto.PetSitterDiaryListResponse;
import com.ajou_nice.with_pet.diary.model.dto.PetSitterDiaryResponse;
import com.ajou_nice.with_pet.service.PetSitterDiaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v1/petsitter-diaries")
@RequiredArgsConstructor
@Api(tags = "PetSitterDiary API")
public class PetSitterDiaryController {

    private final PetSitterDiaryService petSitterDiaryService;

    @PostMapping
    @ApiOperation(value = "펫시터 다이어리 작성")
    public Response<PetSitterDiaryResponse> writePetSitterDiary(
            @ApiIgnore Authentication authentication, @RequestBody DiaryRequest diaryRequest) {
        PetSitterDiaryResponse petSitterDiaryResponse = petSitterDiaryService.writePetsitterDiary(
                authentication.getName(), diaryRequest);
        return Response.success(petSitterDiaryResponse);
    }

    @PutMapping("/{diaryId}")
    @ApiOperation(value = "펫시터 다이어리 수정")
    public Response<PetSitterDiaryResponse> updatePetSitterDiary(
            @ApiIgnore Authentication authentication, @RequestBody DiaryModifyRequest diaryModifyRequest,
            @PathVariable Long diaryId) {
        PetSitterDiaryResponse petSitterDiaryResponse = petSitterDiaryService.updatePetSitterDiary(
                authentication.getName(), diaryModifyRequest, diaryId);
        return Response.success(petSitterDiaryResponse);
    }

    @GetMapping
    @ApiOperation("펫시터가 작성한 반려견에 대한 일지 확인")
    public Response<PetSitterDiaryListResponse> getPetSitterDiaries(
            @ApiIgnore Authentication authentication, @RequestParam Long dogId) {
        return Response.success(
                petSitterDiaryService.getPetSitterDiaries(authentication.getName(), dogId));
    }

    @DeleteMapping("/{diaryId}")
    @ApiOperation("펫시터가 작성한 반려견에 대한 일지 삭제")
    public Response deletePetSitterDiary(@ApiIgnore Authentication authentication, @PathVariable Long diaryId){
        return Response.success(petSitterDiaryService.deletePetSitterDiary(authentication.getName(), diaryId));
    }

}
