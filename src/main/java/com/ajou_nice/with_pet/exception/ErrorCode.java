package com.ajou_nice.with_pet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    PASSWORD_COMPARE_FAIL(HttpStatus.BAD_REQUEST, "Password가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 UserID입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    DOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 반려견이 존재하지 않습니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 그룹이 존재하지 않습니다."),
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일지는 존재하지 않습니다."),
    DUPLICATED_GROUP_MEMBER(HttpStatus.CONFLICT, "해당 그룹에 존재하는 유저입니다."),
    NOT_FOUND_GROUP_MEMBER(HttpStatus.CONFLICT, "해당 그룹에 존재하지 않는 유저입니다."),
    TOO_MANY_GROUP(HttpStatus.FORBIDDEN, "이미 5개의 그룹이 존재하여 그룹을 생성하지 못합니다."),
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
    UNAUTHORIZED_RESERVATION(HttpStatus.UNAUTHORIZED, "해당 예약에 대한 권한이 없습니다."),
    DUPlICATED_SERVICE(HttpStatus.CONFLICT, "해당 이름의 서비스가 이미 존재합니다."),
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅룸입니다."),
    PAYMENT_CANCEL(HttpStatus.OK, "결제를 취소하였습니다."),
    PAYMENT_FAIL(HttpStatus.NOT_ACCEPTABLE, "결제에 실패하였습니다."),
    TO_MANY_APPLICATE(HttpStatus.NOT_ACCEPTABLE, "더 이상 지원할 수 없습니다."),
    PAY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 결제정보입니다."),
    BAD_REQUEST_RESERVATION_STATSUS(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    CAN_NOT_DELETE_DOG(HttpStatus.BAD_REQUEST, "반려견에게 유효한 예약이 존재하기때문에 삭제할 수 없습니다."),
    TOO_MANY_MEMBER(HttpStatus.BAD_REQUEST, "제한 인원 이상의 멤버 수를 추가할 수 없습니다."),
    TOO_MANY_DOG(HttpStatus.BAD_REQUEST, "제한 반려견 수 이상의 반려견 수를 추가할 수 없습니다."),
    TOO_MANY_SMS(HttpStatus.BAD_REQUEST, "잦은 호출로 인한 인증번호 발급이 제한되었습니다."),
    DUPLICATED_PHONE(HttpStatus.CONFLICT, "이미 중복된 휴대폰입니다."),
    CAN_NOT_LEAVE_PARTY(HttpStatus.BAD_REQUEST, "그룹 내 유효한 예약 내역이 존재하기 때문에 그룹을 퇴장할 수 없습니다." ),
    CAN_NOT_EXPEL_PARTY(HttpStatus.BAD_REQUEST, "그룹 내 유효한 예약 내역이 존재하기 때문에 해당 그룹원을 강퇴 시킬 수 없습니다."),
    INCONSISTENCY_AUTHENTICATION_NUMBER(HttpStatus.BAD_REQUEST,"인증번호가 일치하지 않습니다.");

    private HttpStatus status;
    private String message;
}
