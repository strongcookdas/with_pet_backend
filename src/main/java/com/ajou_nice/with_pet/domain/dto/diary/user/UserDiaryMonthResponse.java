package com.ajou_nice.with_pet.domain.dto.diary.user;

import com.ajou_nice.with_pet.domain.entity.UserDiary;
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

    public static UserDiaryMonthResponse of(UserDiary userDiary) {
        return UserDiaryMonthResponse.builder()
                .userDiaryId(userDiary.getUserDiaryId())
                .categoryId(userDiary.getCategory().getCategoryId())
                .categoryName(userDiary.getCategory().getName())
                .dogId(userDiary.getDog().getDogId())
                .dogName(userDiary.getDog().getName())
                .build();
    }
}
