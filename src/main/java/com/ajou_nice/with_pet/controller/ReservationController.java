package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.dog.DogSocializationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @ApiOperation(value = "createReservation")
    public Response createReservation(@ApiIgnore Authentication authentication,
            @RequestBody ReservationRequest reservationRequest) {
        reservationService.createReservation(authentication.getName(), reservationRequest);
        return Response.success("예약이 완료되었습니다.");
    }


    @PutMapping("/update-dogSocialTemperature/{reservationId}")
    @ApiOperation(value = "예약 완료시 펫시터가 반려견 사회화온도 평가")
    public Response modifyDogTemperature(@ApiIgnore Authentication authentication,
            @PathVariable Long reservationId, @RequestBody DogSocializationRequest dogSocializationRequest){
        log.info("---------------------dog Modify socialization Temperature Request : {}--------------------------", dogSocializationRequest);

        reservationService.modifyDogTemp(authentication.getName(), reservationId, dogSocializationRequest);

        Response.success("평가가 완료되었습니다. 감사합니다.");
    }
}
