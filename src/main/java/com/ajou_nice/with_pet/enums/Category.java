package com.ajou_nice.with_pet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    WALK("산책"),
    FOOD("배식"),
    HOSPITAL("병원"),
    MEDICATION("투약"),
    DEFECATION("배변"),
    BATH("목욕"),
    BEAUTY("미용"),
    MEMORY("기념"),
    ETC("기타");
    private String category;
}
