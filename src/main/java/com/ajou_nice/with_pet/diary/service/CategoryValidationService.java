package com.ajou_nice.with_pet.diary.service;

import com.ajou_nice.with_pet.diary.model.entity.Category;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryValidationService {
    private final CategoryRepository categoryRepository;

    public Category validationCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND,
                        ErrorCode.CATEGORY_NOT_FOUND.getMessage()));
    }
}
