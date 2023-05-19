package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.dog.DogSocializationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationDetailResponse;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationResponse;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationStatusRequest;
import com.ajou_nice.with_pet.service.ReservationService;
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
@RequestMapping("api/v1/reservation")
@RequiredArgsConstructor
@Api(tags = "Reservation API")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ApiOperation(value = "예약 하기")
    public Response createReservation(@ApiIgnore Authentication authentication,
            @RequestBody ReservationRequest reservationRequest) {
        log.info("=======================ReservationRequest : {}=============================",reservationRequest);
        reservationService.createReservation(authentication.getName(), reservationRequest);
        return Response.success("예약이 완료되었습니다.");
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
    public Response<List<String>> getUnavailableDates(@ApiIgnore Authentication authentication,
            @RequestParam String month, @RequestParam Long petsitterId) {
        return Response.success(
                reservationService.getUnavailableDates(authentication.getName(), petsitterId,
                        month));
    }

    @PutMapping("/reservation-status")
    @ApiOperation(value = "예약 상태 변경")
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

    @GetMapping("/petsitter/reservations")
    @ApiOperation(value = "펫시터 월별 예약 목록 조회")
    public Response<List<ReservationResponse>> getMonthlyReservations(
            @ApiIgnore Authentication authentication,
            @RequestParam String month) {
        return Response.success(
                reservationService.getMonthlyReservations(authentication.getName(), month));
    }

}
