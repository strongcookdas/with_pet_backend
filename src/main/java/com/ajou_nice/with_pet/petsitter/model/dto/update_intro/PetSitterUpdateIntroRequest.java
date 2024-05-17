package com.ajou_nice.with_pet.petsitter.model.dto.update_intro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PetSitterUpdateIntroRequest {
    @Lob
    private String petSitterIntroduction;
}
