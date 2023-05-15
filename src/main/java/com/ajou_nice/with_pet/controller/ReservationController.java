package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v1/reservation")
@RequiredArgsConstructor
@Api(tags = "Reservation API")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ApiOperation(value = "createReservation")
    public Response createReservation(@ApiIgnore Authentication authentication,
            @RequestBody ReservationRequest reservationRequest) {
        reservationService.createReservation(authentication.getName(), reservationRequest);
        return Response.success("예약이 완료되었습니다.");
    }
}
