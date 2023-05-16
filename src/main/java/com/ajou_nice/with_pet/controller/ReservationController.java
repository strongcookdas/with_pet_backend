package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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
        reservationService.createReservation(authentication.getName(), reservationRequest);
        return Response.success("예약이 완료되었습니다.");
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
    public Response updateReservationStatus(@ApiIgnore Authentication authentication,
            @RequestParam Long reservationId, @RequestParam String status) {
        reservationService.approveReservation(authentication.getName(),reservationId, status);
        return Response.success("예약 상태를 변경했습니다.");
    }

}
