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
public class UserDiaryPostResponse {

    private Long userDiaryId;
    private Long userDiaryCategoryId;
    private String userDiaryCategoryName;
    private Long userDiaryDogId;
    private String userDiaryDogName;
    private String userDiaryDogImg;
    private String userDiaryUserName;
    private String userDiaryTitle;
    private String userDiaryContent;
    private LocalDate userDiaryCreatedAt;

    public static UserDiaryPostResponse of(Diary diary) {

        return UserDiaryPostResponse.builder()
                .userDiaryId(diary.getDiaryId())
                .userDiaryCategoryId(diary.getDiaryCategory().getCategoryId())
                .userDiaryDogId(diary.getDog().getDogId())
                .userDiaryCategoryName(diary.getDiaryCategory().getCategoryName())
                .userDiaryUserName(diary.getUser().getName())
                .userDiaryTitle(diary.getDiaryTitle())
                .userDiaryContent(diary.getDiaryContent())
                .userDiaryDogImg(diary.getDiaryMedia())
                .userDiaryDogName(diary.getDog().getDogName())
                .userDiaryCreatedAt(diary.getCreatedAt())
                .build();
    }
}
