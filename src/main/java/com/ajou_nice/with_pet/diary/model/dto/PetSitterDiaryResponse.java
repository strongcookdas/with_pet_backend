package com.ajou_nice.with_pet.diary.model.dto;

import com.ajou_nice.with_pet.diary.model.entity.Diary;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PetSitterDiaryResponse {

    private Long petSitterDiaryId;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String contentBody;
    private String dogImgToday;
    private LocalDate createdAt;

    public static PetSitterDiaryResponse of(Diary diary) {
        return PetSitterDiaryResponse.builder()
                .petSitterDiaryId(diary.getDiaryId())
                .categoryId(diary.getDiaryCategory().getCategoryId())
                .categoryName(diary.getDiaryCategory().getCategoryName())
                .title(diary.getDiaryTitle())
                .contentBody(diary.getDiaryContent())
                .dogImgToday(diary.getDiaryMedia())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}
