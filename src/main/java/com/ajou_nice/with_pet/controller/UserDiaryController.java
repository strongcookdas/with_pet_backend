package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.domain.dto.diary.user.UserDiaryMonthResponse;
import com.ajou_nice.with_pet.domain.dto.diary.user.UserDiaryResponse;
import com.ajou_nice.with_pet.service.UserDiaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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

    /**
     * 사용자의 반려견 일지 조회 (월 기준)
     *
     * @param authentication 사용자 인증
     * @param dogId          반려견 필터링 null 값이 들어올 수 있다.
     * @param categoryId     카테고리 피터링 null 값이 들어올 수 있다.
     * @param month          월 기준 피터링 null 값이 들어오면 안되지만 들어올 수 있기에 리팩토링 필요
     * @return 조회 조건에 부합하는 일지를 리스트화해서 리턴
     */
    @GetMapping("/month")
    @ApiOperation(value = "월별 캘린더 일지 조회")
    @ApiImplicitParam(name = "month", value = "해당 년 월", example = "2023-05", required = true, dataTypeClass = String.class)
    public Response<List<UserDiaryMonthResponse>> getUserMonthDiary(
            @ApiIgnore Authentication authentication,
            @RequestParam(required = false) Long dogId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam String month) {

        List<UserDiaryMonthResponse> userDiaryResponses = userDiaryService.getUserMonthDiary(
                authentication.getName(), dogId, categoryId, month);
        return Response.success(userDiaryResponses);
    }

    /**
     * 사용자의 반려견 일지 조회 (월 기준)
     *
     * @param authentication 사용자 인증
     * @param dogId          반려견 필터링 null 값이 들어올 수 있다.
     * @param categoryId     카테고리 피터링 null 값이 들어올 수 있다.
     * @param day            월 기준 피터링 null 값이 들어오면 안되지만 들어올 수 있기에 리팩토링 필요
     * @return 조회 조건에 부합하는 일지를 리스트화해서 리턴
     */
    @GetMapping("/day")
    @ApiOperation(value = "일별 캘린더 일지 조회")
    @ApiImplicitParam(name = "day", value = "해당 년 월 일", example = "2023-05-12", required = true, dataTypeClass = String.class)
    public Response<List<UserDiaryResponse>> getUserDayDiary(
            @ApiIgnore Authentication authentication,
            @RequestParam(required = false) Long dogId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam String day) {

        List<UserDiaryResponse> userDiaryResponses = userDiaryService.getUserDayDiary(
                authentication.getName(), dogId, categoryId, day);
        return Response.success(userDiaryResponses);
    }
}


