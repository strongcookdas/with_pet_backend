package com.ajou_nice.with_pet.dog.model.dto;

import com.ajou_nice.with_pet.dog.model.entity.Dog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DogSimpleInfoResponse {

    private Long dogId;
    private String dogImg;
    private String name;

    public static DogSimpleInfoResponse of(Dog dog) {
        return DogSimpleInfoResponse.builder()
                .dogId(dog.getDogId())
                .dogImg(dog.getDogProfileImg())
                .name(dog.getDogName())
                .build();
    }
}
