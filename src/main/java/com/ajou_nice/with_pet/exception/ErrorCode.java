package com.ajou_nice.with_pet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "UserID가 중복됩니다."),
    PASSWORD_COMPARE_FAIL(HttpStatus.BAD_REQUEST, "Password가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 UserID입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다."),
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
    private HttpStatus status;
    private String message;
}
