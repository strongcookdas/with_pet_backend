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
    APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "지원 정보가 없습니다."),
    DUPLICATED_APPLICATION(HttpStatus.CONFLICT, "중복 지원은 불가능합니다."),
    BAD_REQUEST_APPLICANT_STATUS(HttpStatus.BAD_REQUEST, "잘못된 지원자 상태 변경 요청입니다."),
    WITH_PET_SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 서비스입니다."),
    PETSITTER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 펫시터가 없습니다.");
    private HttpStatus status;
    private String message;
}
