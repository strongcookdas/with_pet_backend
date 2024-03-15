package com.ajou_nice.with_pet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ValidMessages {
    INVALID_EMAIL("올바른 이메일 형식을 입력하세요.");
    private String message;
}
