package com.ajou_nice.with_pet.domain.dto.diary.user;

import com.ajou_nice.with_pet.domain.entity.UserDiary;
import com.ajou_nice.with_pet.enums.Category;
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
    private Category category;
    private String content;
    private String media;

    public static UserDiaryResponse of(UserDiary userDiary) {
        return UserDiaryResponse.builder()
                .userDiaryId(userDiary.getUserDiaryId())
                .category(userDiary.getCategory())
                .content(userDiary.getContent())
                .media(userDiary.getMedia())
                .build();
    }
}
