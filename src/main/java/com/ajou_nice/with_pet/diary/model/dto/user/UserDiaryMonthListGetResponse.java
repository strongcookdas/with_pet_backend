package com.ajou_nice.with_pet.diary.model.dto.user;

import com.ajou_nice.with_pet.diary.model.entity.Diary;
import java.util.List;
import java.util.stream.Collectors;
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
public class UserDiaryMonthListGetResponse {
    private List<UserDiaryMonthGetResponse> userDiaryMonthGetResponses;

    public static UserDiaryMonthListGetResponse of(List<Diary> diaries) {
        return UserDiaryMonthListGetResponse.builder()
                .userDiaryMonthGetResponses(
                        diaries.stream().map(UserDiaryMonthGetResponse::of).collect(Collectors.toList()))
                .build();
    }
}
