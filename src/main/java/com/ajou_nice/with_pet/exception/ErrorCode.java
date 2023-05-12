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
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    DOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 반려견이 존재하지 않습니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 그룹이 존재하지 않습니다."),
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일지는 존재하지 않습니다."),
    DUPLICATED_GROUP_MEMBER(HttpStatus.CONFLICT, "해당 그룹에 존재하는 유저입니다.");
    private HttpStatus status;
    private String message;
}
