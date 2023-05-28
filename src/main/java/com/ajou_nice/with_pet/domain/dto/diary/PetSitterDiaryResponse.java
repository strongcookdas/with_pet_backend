package com.ajou_nice.with_pet.domain.dto.diary;

import com.ajou_nice.with_pet.domain.entity.Diary;
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

    private Long userDiaryId;
    private String categoryName;
    private String title;
    private String contentBody;
    private String dogImgToday;
    private LocalDate createdAt;

    public static PetSitterDiaryResponse of(Diary diary) {
        return PetSitterDiaryResponse.builder()
                .userDiaryId(diary.getDiaryId())
                .categoryName(diary.getCategory().getName())
                .title(diary.getTitle())
                .contentBody(diary.getContent())
                .dogImgToday(diary.getMedia())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}
