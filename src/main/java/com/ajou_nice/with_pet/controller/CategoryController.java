package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.category.CategoryRequest;
import com.ajou_nice.with_pet.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
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
            CategoryRequest categoryRequest) {

        categoryService.addCategory(authentication.getName(),categoryRequest);

        return Response.success("카테고리 추가되었습니다.");
    }
}
