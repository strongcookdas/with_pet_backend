package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.category.CategoryRequest;
import com.ajou_nice.with_pet.domain.dto.category.CategoryResponse;
import com.ajou_nice.with_pet.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@Api(tags = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ApiOperation(value = "카테고리 추가")
    public Response addCategory(@ApiIgnore Authentication authentication,
            @RequestBody CategoryRequest categoryRequest) {

        categoryService.addCategory(authentication.getName(), categoryRequest);

        return Response.success("카테고리 추가되었습니다.");
    }

    @GetMapping
    @ApiOperation(value = "카테고리 조회")
    public Response<List<CategoryResponse>> getCategories(
            @ApiIgnore Authentication authentication) {
        return Response.success(categoryService.getCategoryList());
    }
}
