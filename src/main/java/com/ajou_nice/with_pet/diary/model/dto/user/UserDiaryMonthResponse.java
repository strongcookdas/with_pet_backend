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
public class UserDiaryMonthResponse {

    private Long userDiaryId;
    private Long categoryId;
    private String categoryName;
    private Long dogId;
    private String dogName;
    private String createdAt;

    public static UserDiaryMonthResponse of(Diary diary) {
        return UserDiaryMonthResponse.builder()
                .userDiaryId(diary.getDiaryId())
                .categoryId(diary.getCategory().getCategoryId())
                .categoryName(diary.getCategory().getName())
                .dogId(diary.getDog().getDogId())
                .dogName(diary.getDog().getDogName())
                .createdAt(diary.getCreatedAt().toString())
                .build();
    }
}
