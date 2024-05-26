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
    private Long petsitterId;
    private Long dogId;
    private String categoryName;
    private String userName;
    private String title;
    private String contentBody;
    private String dogImgToday;
    private String dogName;
    private LocalDate createdAt;

    public static UserDiaryResponse of(Diary diary) {

        Long petsitterId = null;
        if(diary.getPetSitter()!=null){
            petsitterId = diary.getPetSitter().getId();
        }

        return UserDiaryResponse.builder()
                .userDiaryId(diary.getDiaryId())
                .categoryId(diary.getCategory().getCategoryId())
                .petsitterId(petsitterId)
                .dogId(diary.getDog().getDogId())
                .categoryName(diary.getCategory().getName())
                .userName(diary.getUser().getName())
                .title(diary.getTitle())
                .contentBody(diary.getContent())
                .dogImgToday(diary.getMedia())
                .dogName(diary.getDog().getDogName())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}
