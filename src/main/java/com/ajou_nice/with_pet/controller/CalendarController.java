package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.calendar.PetSitterSideBarResponse;
import com.ajou_nice.with_pet.domain.dto.calendar.SideBarResponse;
import com.ajou_nice.with_pet.domain.dto.category.CategoryResponse;
import com.ajou_nice.with_pet.domain.dto.dog.DogSimpleInfoResponse;
import com.ajou_nice.with_pet.service.CalendarService;
import com.ajou_nice.with_pet.service.CategoryService;
import com.ajou_nice.with_pet.service.DogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final DogService dogService;
    private final CategoryService categoryService;
    private final CalendarService calendarService;

    //사이드바에 필요한 정보 조회
    @GetMapping
    public Response<SideBarResponse> getSideBarInfo(@ApiIgnore Authentication authentication) {
        List<DogSimpleInfoResponse> dogSimpleInfoResponses = dogService.getDogSimpleInfos(
                authentication.getName());
        List<CategoryResponse> categoryResponses = categoryService.getCategoryList();
        return Response.success(SideBarResponse.of(dogSimpleInfoResponses, categoryResponses));
    }

    @GetMapping("/petsitter-calendar")
    public Response<PetSitterSideBarResponse> getPetSitterSideBarInfo(
            @ApiIgnore Authentication authentication, @RequestParam String month) {
        return Response.success(
                calendarService.getPetSitterSideBarInfo(authentication.getName(), month));
    }
}
