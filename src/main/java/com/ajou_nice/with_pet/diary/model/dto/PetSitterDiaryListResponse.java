package com.ajou_nice.with_pet.diary.model.dto;

import com.ajou_nice.with_pet.diary.model.entity.Diary;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
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
public class PetSitterDiaryListResponse {

    private String dogImg;
    private String dogName;
    private List<PetSitterDiaryResponse> petSitterDiaryResponses;

    public static PetSitterDiaryListResponse of(Dog dog, List<Diary> diaries) {
        return PetSitterDiaryListResponse.builder()
                .dogImg(dog.getDogProfileImg())
                .dogName(dog.getDogName())
                .petSitterDiaryResponses(diaries.stream().map(PetSitterDiaryResponse::of).collect(
                        Collectors.toList()))
                .build();
    }
}
