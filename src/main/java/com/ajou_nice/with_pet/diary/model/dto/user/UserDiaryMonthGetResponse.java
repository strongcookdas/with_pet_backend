package com.ajou_nice.with_pet.diary.model.dto.user;

import com.ajou_nice.with_pet.diary.model.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserDiaryMonthGetResponse {

    private Long userDiaryId;
    private Long categoryId;
    private String categoryName;
    private Long dogId;
    private String dogName;
    private String createdAt;

    public static UserDiaryMonthGetResponse of(Diary diary) {
        return UserDiaryMonthGetResponse.builder()
                .userDiaryId(diary.getDiaryId())
                .categoryId(diary.getDiaryCategory().getCategoryId())
                .categoryName(diary.getDiaryCategory().getCategoryName())
                .dogId(diary.getDog().getDogId())
                .dogName(diary.getDog().getDogName())
                .createdAt(diary.getCreatedAt().toString())
                .build();
    }
}
