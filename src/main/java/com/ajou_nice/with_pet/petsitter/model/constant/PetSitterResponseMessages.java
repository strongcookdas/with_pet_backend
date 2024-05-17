package com.ajou_nice.with_pet.petsitter.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PetSitterResponseMessages {
    HOUSE_UPDATE("수정이 완료되었습니다."),
    HASHTAG_UPDATE("수정이 완료되었습니다.");
    private String message;
}
