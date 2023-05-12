package com.ajou_nice.with_pet.domain.dto.dog;

import com.ajou_nice.with_pet.domain.entity.Dog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
                .dogImg(dog.getProfile_img())
                .name(dog.getName())
                .build();
    }
}
