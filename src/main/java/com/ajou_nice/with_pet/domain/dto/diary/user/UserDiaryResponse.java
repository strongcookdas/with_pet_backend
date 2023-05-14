package com.ajou_nice.with_pet.domain.dto.diary.user;

import com.ajou_nice.with_pet.domain.entity.UserDiary;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class UserDiaryResponse {

    private Long userDiaryId;
    private Long categoryId;
    private String categoryName;
    private String content;
    private String media;
    private LocalDate createdAt;

    public static UserDiaryResponse of(UserDiary userDiary) {
        return UserDiaryResponse.builder()
                .userDiaryId(userDiary.getUserDiaryId())
                .categoryId(userDiary.getCategory().getCategoryId())
                .categoryName(userDiary.getCategory().getName())
                .content(userDiary.getContent())
                .media(userDiary.getMedia())
                .createdAt(userDiary.getCreatedAt())
                .build();
    }
}