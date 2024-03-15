package com.ajou_nice.with_pet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ValidMessages {
    INVALID_EMAIL("올바른 이메일 형식을 입력하세요.", ""),
    INVALID_NAME("올바른 이름을 입력하세요.", "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$"),
    INVALID_PASSWORD("영문, 특수문자, 숫자 포함 8자 이상의 패스워드를 입력하세요.", "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$"),
    INVALID_PASSWORD_CHECK("패스워드 확인란에 패스워드를 입력하세요.", ""),
    INVALID_PHONE("올바른 형식의 전화번호를 입력하세요.", "^\\d{2,3}-\\d{3,4}-\\d{4}$");
    private String message;
    private String REX;
}
