package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.domain.dto.diary.user.UserDiaryResponse;
import com.ajou_nice.with_pet.service.UserDiaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/userdiaries")
@Api(tags = "UserDiary API")
@Slf4j
public class UserDiaryController {

    private final UserDiaryService userDiaryService;

    @PostMapping
    @ApiOperation(value = "반려인 다이어리 작성")
    public Response<UserDiaryResponse> writeUserDiary(@ApiIgnore Authentication authentication,
            @Valid @RequestBody DiaryRequest diaryRequest) {
        log.info("token : {}", authentication.getName());
        log.info(
                "-------------------------------------------DiaryRequest : {}------------------------------------------------",
                diaryRequest);
        UserDiaryResponse userDiaryResponse = userDiaryService.writeUserDiary(
                authentication.getName(), diaryRequest);
        log.info(
                "-------------------------------------------DiaryResponse : {}------------------------------------------------",
                userDiaryResponse);
        return Response.success(userDiaryResponse);
    }

    @PutMapping("/{diaryId}")
    @ApiOperation(value = "반려인 다이어리 수정")
    public Response<UserDiaryResponse> updateUserDiary(@ApiIgnore Authentication authentication,
            @Valid @RequestBody DiaryRequest diaryRequest, @PathVariable Long diaryId) {
        UserDiaryResponse userDiaryResponse = userDiaryService.updateUserDiary(
                authentication.getName(), diaryRequest, diaryId);
        return Response.success(userDiaryResponse);
    }

//    @GetMapping("/{dogId}")
//    public Response<UserDiaryResponse> getUserDiary(@ApiIgnore Authentication authentication,
//            @PathVariable Long dogId) {
//        return Response.success(
//                userDiaryService.getUserDiary(authentication.getName(),dogId);
//        )
//    }


}
