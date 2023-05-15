package com.ajou_nice.with_pet.domain.dto.category;

import com.ajou_nice.with_pet.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CategoryResponse {

    private Long categoryId;
    private String name;

    public static CategoryResponse of(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}
