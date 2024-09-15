package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.calendar.SideBarResponse;
import com.ajou_nice.with_pet.domain.dto.category.CategoryResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogSimpleInfoResponse;
import com.ajou_nice.with_pet.service.CategoryService;
import com.ajou_nice.with_pet.dog.service.DogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v1/calendar")
@RequiredArgsConstructor
@Api(tags = "Calendar API")
public class CalendarController {

    private final DogService dogService;
    private final CategoryService categoryService;


    //사이드바에 필요한 정보 조회
    @GetMapping
    @ApiOperation(value = "반려인 캘린더 사이드바 조회")
    public Response<SideBarResponse> getSideBarInfo(@ApiIgnore Authentication authentication) {
        List<DogSimpleInfoResponse> dogSimpleInfoResponses = dogService.getDogSimpleInfos(
                authentication.getName());
        List<CategoryResponse> categoryResponses = categoryService.getCategoryList();
        return Response.success(SideBarResponse.of(dogSimpleInfoResponses, categoryResponses));
    }
}
