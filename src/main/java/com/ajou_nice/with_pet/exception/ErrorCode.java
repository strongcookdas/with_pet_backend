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
    DUPLICATED_GROUP_MEMBER(HttpStatus.CONFLICT, "해당 그룹에 존재하는 유저입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."),
    APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "지원 정보가 없습니다."),
    DUPLICATED_APPLICATION(HttpStatus.CONFLICT, "중복 지원은 불가능합니다."),
    BAD_REQUEST_APPLICANT_STATUS(HttpStatus.BAD_REQUEST, "잘못된 지원자 상태 변경 요청입니다."),
    WITH_PET_SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 서비스입니다."),
    CRITICAL_SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 서비스입니다."),
    PETSITTER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 펫시터가 없습니다."),
    PETSITTER_SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 서비스입니다."),
    PETSITTER_HOUSE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 집 정보가 없습니다."),
    PETSITTER_HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 해시태그가 없습니다."),
    PETSITTER_MAIN_HOUSE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 대표 집 사진이 없습니다."),
    DUPLICATED_RESERVATION(HttpStatus.CONFLICT, "해당 반려견은 예약할 수 없습니다."),
    RESERVATION_CONFLICT(HttpStatus.CONFLICT, "해당 기간에는 예약할 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예약을 찾을 수 없습니다."),
    UNAUTHORIZED_RESERVATION(HttpStatus.UNAUTHORIZED, "해당 예약에 대한 권한이 없습니다.");
    private HttpStatus status;
    private String message;
}
