package com.ajou_nice.with_pet.reservation.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.dog.model.dto.DogSocializationRequest;
import com.ajou_nice.with_pet.domain.dto.kakaopay.RefundResponse;
import com.ajou_nice.with_pet.reservation.model.dto.PaymentResponseForPetSitter;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationCreateResponse;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationDetailResponse;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationDocsResponse;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationCreateRequest;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationCreateRequest.ReservationSimpleRequest;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationResponse;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationStatusRequest;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.service.KaKaoPayService;
import com.ajou_nice.with_pet.reservation.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("api/v2/reservation")
@RequiredArgsConstructor
@Api(tags = "Reservation API")
public class ReservationController {

    private final ReservationService reservationService;
    private final KaKaoPayService kaKaoPayService;

    @PostMapping
    @ApiOperation(value = "예약 하기")
    public Response<ReservationCreateResponse> createReservation(@ApiIgnore Authentication authentication, @RequestBody ReservationCreateRequest reservationCreateRequest) {
        ReservationCreateResponse reservationCreateResponse = reservationService.createReservation(authentication.getName(), reservationCreateRequest);
        return Response.success(reservationCreateResponse);
    }


    @PutMapping("/update-dogSocialTemperature/{reservationId}")
    @ApiOperation(value = "예약 완료시 펫시터가 반려견 사회화온도 평가")
    public Response modifyDogTemperature(@ApiIgnore Authentication authentication,
            @PathVariable Long reservationId,
            @RequestBody DogSocializationRequest dogSocializationRequest) {
        log.info(
                "---------------------dog Modify socialization Temperature Request : {}--------------------------",
                dogSocializationRequest);

        reservationService.modifyDogTemp(authentication.getName(), reservationId,
                dogSocializationRequest);

        return Response.success("평가가 완료되었습니다. 감사합니다.");
    }

    @GetMapping
    @ApiOperation(value = "예약 불가능한 날짜 반환")
    @ApiImplicitParam(name = "month", value = "해당 년 월", example = "2023-05", required = true, dataTypeClass = String.class)
    public Response<List<String>> getUnavailableDates(@RequestParam String month,
            @RequestParam Long petsitterId) {
        return Response.success(
                reservationService.getUnavailableDates(petsitterId, month));
    }

    @PutMapping("/reservation-accept")
    @ApiOperation(value = "예약 수락")
    public Response<ReservationDetailResponse> updateReservationStatus(
            @ApiIgnore Authentication authentication,
            @RequestBody ReservationStatusRequest reservationStatusRequest) {
        log.info(
                "============================ReservationStatusRequest : {}==============================",
                reservationStatusRequest);
        ReservationDetailResponse detailResponse = reservationService.approveReservation(
                authentication.getName(),
                reservationStatusRequest.getReservationId(), reservationStatusRequest.getStatus());
        return Response.success(detailResponse);
    }

    @PostMapping("/reservation-refuse")
    @ApiOperation(value = "펫시터의 예약 거절")
    public Response<RefundResponse> refuseReservation(@ApiIgnore Authentication authentication,
            @RequestBody ReservationStatusRequest reservationStatusRequest) {
        log.info(
                "============================ReservationStatusRequest : {}==============================",
                reservationStatusRequest);
        if (!reservationStatusRequest.getStatus().equals("REFUSE")) {
            throw new AppException(ErrorCode.BAD_REQUEST_RESERVATION_STATSUS,
                    ErrorCode.BAD_REQUEST_APPLICANT_STATUS.getMessage());
        }
        RefundResponse refundResponse = kaKaoPayService.refundPayment(authentication.getName(),
                reservationStatusRequest.getReservationId());

        log.info("=======================payCancelResponse : {}=============================",
                refundResponse);
        return Response.success(refundResponse);
    }

    @GetMapping("/petsitter/reservations")
    @ApiOperation(value = "펫시터 월별 예약 목록 조회")
    public Response<List<ReservationResponse>> getMonthlyReservations(
            @ApiIgnore Authentication authentication,
            @RequestParam String month) {
        return Response.success(
                reservationService.getMonthlyReservations(authentication.getName(), month));
    }

    @PostMapping("/user/done-reservation")
    @ApiOperation(value = "사용자의 이용 완료 신청")
    public Response doneReservation(@ApiIgnore Authentication authentication,
            @RequestBody ReservationSimpleRequest simpleRequest) {
        reservationService.doneReservation(authentication.getName(),
                simpleRequest.getReservationId());

        return Response.success("완료 되었습니다. 만족스러우셨다면 후기를 작성해주세요.");
    }

    @PostMapping("/user/cancel-reservation")
    @ApiOperation(value = "사용자의 예약 취소 (결제 전 예약건에 대해)")
    public Response cancelReservation(@ApiIgnore Authentication authentication,
            @RequestBody ReservationSimpleRequest simpleRequest) {

        reservationService.cancelReservation(authentication.getName(),
                simpleRequest.getReservationId());

        return Response.success("취소가 완료 되었습니다.");
    }

    @GetMapping("/user/show-reservations")
    @ApiOperation(value = "유저의 예약 리스트 조회")
    public Response<ReservationDocsResponse> getMyReservations(@ApiIgnore Authentication authentication){

        ReservationDocsResponse docsResponse = reservationService.getReservationDoc(authentication.getName());

        return Response.success(docsResponse);
    }

    @GetMapping("/show-payment/{reservationId}")
    @ApiOperation(value = "펫시터의 예약에 대한 결제내역 확인")
    public Response<PaymentResponseForPetSitter> showPaymentForPetSitter(@ApiIgnore Authentication authentication,
            @PathVariable("reservationId") Long reservationId){

        PaymentResponseForPetSitter paymentResponseForPetSitter = reservationService.getPaymentView(
                authentication.getName(), reservationId);

        return Response.success(paymentResponseForPetSitter);
    }

}
