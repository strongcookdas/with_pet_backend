package com.ajou_nice.with_pet.diary.model.dto.user;

import com.ajou_nice.with_pet.diary.model.entity.Diary;
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
                .categoryId(diary.getDiaryCategory().getCategoryId())
                .petsitterId(petsitterId)
                .dogId(diary.getDog().getDogId())
                .categoryName(diary.getDiaryCategory().getCategoryName())
                .userName(diary.getUser().getName())
                .title(diary.getDiaryTitle())
                .contentBody(diary.getDiaryContent())
                .dogImgToday(diary.getDiaryMedia())
                .dogName(diary.getDog().getDogName())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}
