package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.category.CategoryRequest;
import com.ajou_nice.with_pet.domain.dto.category.CategoryResponse;
import com.ajou_nice.with_pet.domain.entity.Category;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CategoryRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ValidateCollection valid;

    public void addCategory(String userId, CategoryRequest categoryRequest) {
        //유저 체크
        valid.userValidation(userId);
        //카테고리 추가
        categoryRepository.save(Category.of(categoryRequest));
    }

    public List<CategoryResponse> getCategoryList() {
        return categoryRepository.findAll().stream().map(CategoryResponse::of)
                .collect(Collectors.toList());
    }
}
