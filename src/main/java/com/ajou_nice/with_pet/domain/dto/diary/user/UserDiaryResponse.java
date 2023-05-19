package com.ajou_nice.with_pet.domain.dto.diary.user;

import com.ajou_nice.with_pet.domain.entity.Diary;
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
    private String title;
    private String content;
    private String media;
    private String dogName;
    private LocalDate createdAt;

    public static UserDiaryResponse of(Diary diary) {
        return UserDiaryResponse.builder()
                .userDiaryId(diary.getDiaryId())
                .categoryId(diary.getCategory().getCategoryId())
                .categoryName(diary.getCategory().getName())
                .title(diary.getTitle())
                .content(diary.getContent())
                .media(diary.getMedia())
                .dogName(diary.getDog().getName())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}
