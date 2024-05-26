package com.ajou_nice.with_pet.dog.model.dto.delete;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DogDeleteResponse {
    private Boolean isDeletedParty;

    public static DogDeleteResponse of(Boolean isDeletedParty) {
        return DogDeleteResponse.builder().isDeletedParty(isDeletedParty).build();
    }
}
