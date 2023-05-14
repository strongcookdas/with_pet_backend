package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.category.CategoryRequest;
import com.ajou_nice.with_pet.domain.dto.category.CategoryResponse;
import com.ajou_nice.with_pet.service.CategoryService;
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
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public Response addCategory(@ApiIgnore Authentication authentication,
            @RequestBody CategoryRequest categoryRequest) {

        categoryService.addCategory(authentication.getName(), categoryRequest);

        return Response.success("카테고리 추가되었습니다.");
    }

    @GetMapping
    public Response<List<CategoryResponse>> getCategories(
            @ApiIgnore Authentication authentication) {
        return Response.success(categoryService.getCategoryList());
    }
}
