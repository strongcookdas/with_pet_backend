package com.ajou_nice.with_pet.diary.model.entity;

import com.ajou_nice.with_pet.domain.dto.category.CategoryRequest;
import com.ajou_nice.with_pet.domain.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    public static Category of(CategoryRequest categoryRequest) {
        return Category.builder()
                .categoryName(categoryRequest.getName())
                .build();
    }

    public static Category simpleCategoryForTest(String name) {
        return Category.builder()
                .categoryName(name)
                .build();
    }

}
