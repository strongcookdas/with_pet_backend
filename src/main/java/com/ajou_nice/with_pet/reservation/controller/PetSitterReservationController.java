package com.ajou_nice.with_pet.reservation.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.pay.model.dto.RefundResponse;
import com.ajou_nice.with_pet.pay.service.KaKaoPayService;
import com.ajou_nice.with_pet.reservation.model.dto.PaymentResponseForPetSitter;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationGetMonthlyResponse;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationGetSideInfoResponse;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationPatchApprovalResponse;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationPutEvaluateDogRequest;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationPutEvaluateDogResponse;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationCreateRequest.ReservationSimpleRequest;
import com.ajou_nice.with_pet.reservation.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("api/v2/pet-sitters/reservations")
@RequiredArgsConstructor
@Api(tags = "PetSitter Reservation API")
public class PetSitterReservationController {

    private final ReservationService PetSitterReservationService;
    private final KaKaoPayService kaKaoPayService;

    @GetMapping
    @ApiOperation(value = "펫시터 월별 예약 목록 조회")
    @ApiImplicitParam(name = "month", value = "해당 년 월", example = "2024-06", required = true, dataTypeClass = String.class)
    public Response<List<PetSitterReservationGetMonthlyResponse>> getMonthlyReservations(
            @ApiIgnore Authentication authentication,
            @RequestParam String month) {
        return Response.success(PetSitterReservationService.getMonthlyReservations(authentication.getName(), month));
    }

    @GetMapping("/{petSitterId}/unavailable-dates")
    @ApiOperation(value = "펫시터 예약 불가능한 날짜 반환")
    @ApiImplicitParam(name = "month", value = "해당 년 월", example = "2024-06", required = true, dataTypeClass = String.class)
    public Response<List<String>> getUnavailableDates(@RequestParam String month, @PathVariable Long petSitterId) {
        return Response.success(PetSitterReservationService.getUnavailableDates(petSitterId, month));
    }

    @PatchMapping("/approval/{reservationId}")
    @ApiOperation(value = "펫시터 예약 승인")
    public Response<PetSitterReservationPatchApprovalResponse> approveReservation(
            @ApiIgnore Authentication authentication,
            @PathVariable Long reservationId) {
        return Response.success(
                PetSitterReservationService.approveReservation(authentication.getName(), reservationId));
    }

    @PutMapping("/dog-social-temperature/{reservationId}")
    @ApiOperation(value = "예약 완료시 펫시터 반려견 사회화온도 평가")
    public Response<PetSitterReservationPutEvaluateDogResponse> evaluateDogSocialTemperature(
            @ApiIgnore Authentication authentication, @PathVariable Long reservationId,
            @RequestBody PetSitterReservationPutEvaluateDogRequest dogSocializationRequest) {
        return Response.success(
                PetSitterReservationService.evaluateDogSocialTemperature(authentication.getName(), reservationId,
                        dogSocializationRequest));
    }

    @PatchMapping("/refuse/{reservationId}")
    @ApiOperation(value = "펫시터의 예약 거절")
    public Response<RefundResponse> refuseReservation(@ApiIgnore Authentication authentication,
                                                      @PathVariable Long reservationId) {
        RefundResponse refundResponse = kaKaoPayService.refundPayment(authentication.getName(), reservationId);
        return Response.success(refundResponse);
    }


    @PostMapping("/user/done-reservation")
    @ApiOperation(value = "사용자의 이용 완료 신청")
    public Response doneReservation(@ApiIgnore Authentication authentication,
                                    @RequestBody ReservationSimpleRequest simpleRequest) {
        PetSitterReservationService.doneReservation(authentication.getName(),
                simpleRequest.getReservationId());

        return Response.success("완료 되었습니다. 만족스러우셨다면 후기를 작성해주세요.");
    }

    @PostMapping("/user/cancel-reservation")
    @ApiOperation(value = "사용자의 예약 취소 (결제 전 예약건에 대해)")
    public Response cancelReservation(@ApiIgnore Authentication authentication,
                                      @RequestBody ReservationSimpleRequest simpleRequest) {

        PetSitterReservationService.cancelReservation(authentication.getName(),
                simpleRequest.getReservationId());

        return Response.success("취소가 완료 되었습니다.");
    }

    @GetMapping("/show-payment/{reservationId}")
    @ApiOperation(value = "펫시터의 예약에 대한 결제내역 확인")
    public Response<PaymentResponseForPetSitter> showPaymentForPetSitter(@ApiIgnore Authentication authentication,
                                                                         @PathVariable("reservationId") Long reservationId) {

        PaymentResponseForPetSitter paymentResponseForPetSitter = PetSitterReservationService.getPaymentView(
                authentication.getName(), reservationId);

        return Response.success(paymentResponseForPetSitter);
    }

    @GetMapping("/side-infos")
    @ApiOperation(value = "유형별 예약 건 조회 (펫시터 캘린더 사이드바)")
    public Response<PetSitterReservationGetSideInfoResponse> getPetSitterSideBarInfo(
            @ApiIgnore Authentication authentication,
            @RequestParam String month) {
        return Response.success(PetSitterReservationService.getPetSitterReservationsByType(authentication.getName(), month));
    }

}
