package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.domain.dto.diary.PetSitterDiaryResponse;
import com.ajou_nice.with_pet.service.PetSitterDiaryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v1/petsitter-diaries")
@RequiredArgsConstructor
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
    @ApiIgnore(value = "펫시터 다이어리 수정")
    public Response<PetSitterDiaryResponse> updatePetSitterDiary(
            @ApiIgnore Authentication authentication, @RequestBody DiaryRequest diaryRequest,
            @PathVariable Long diaryId) {
        PetSitterDiaryResponse petSitterDiaryResponse = petSitterDiaryService.updatePetSitterDiary(
                authentication.getName(), diaryRequest, diaryId);
        return Response.success(petSitterDiaryResponse);
    }
}
