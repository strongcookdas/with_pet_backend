package com.ajou_nice.with_pet.domain.dto.dog;

import com.ajou_nice.with_pet.domain.entity.Dog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DogListInfoResponse {

    private Long dogId;
    private String name;
    private Boolean petReservationAvailable = false;

    public static DogListInfoResponse of(Dog dog, boolean check) {
        return DogListInfoResponse.builder()
                .dogId(dog.getDogId())
                .name(dog.getName())
                .petReservationAvailable(check)
                .build();
    }
}
