package com.ajou_nice.with_pet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "UserID가 중복됩니다."),
    PASSWORD_COMPARE_FAIL(HttpStatus.BAD_REQUEST, "Password가 일치하지 않습니다.");
    private HttpStatus status;
    private String message;
}
