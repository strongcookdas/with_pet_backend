package com.ajou_nice.with_pet.reservation.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationCreateRequest;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationCreateResponse;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationGetInfosResponse;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationPatchCancelResponse;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationPatchDoneResponse;
import com.ajou_nice.with_pet.reservation.service.UserReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("api/v2/users/reservations")
@RequiredArgsConstructor
@Api(tags = "User Reservation API")
public class UserReservationController {

    private final UserReservationService userReservationService;

    @GetMapping
    @ApiOperation(value = "유저의 예약 리스트 조회")
    public Response<UserReservationGetInfosResponse> getUserReservations(@ApiIgnore Authentication authentication) {
        UserReservationGetInfosResponse docsResponse = userReservationService.getUserReservations(
                authentication.getName());
        return Response.success(docsResponse);
    }

    @PostMapping
    @ApiOperation(value = "예약 하기")
    public Response<UserReservationCreateResponse> createReservation(@ApiIgnore Authentication authentication,
                                                                     @RequestBody ReservationCreateRequest reservationCreateRequest) {
        UserReservationCreateResponse userReservationCreateResponse = userReservationService.createReservation(
                authentication.getName(), reservationCreateRequest);
        return Response.success(userReservationCreateResponse);
    }

    @PatchMapping("/done/{reservationId}")
    @ApiOperation(value = "사용자의 이용 완료")
    public Response<UserReservationPatchDoneResponse> doneReservation(@ApiIgnore Authentication authentication,
                                                                      @PathVariable Long reservationId) {
        UserReservationPatchDoneResponse doneResponse = userReservationService.doneReservation(authentication.getName(),
                reservationId);
        return Response.success(doneResponse);
    }

    @PatchMapping("/cancel/{reservationId}")
    @ApiOperation(value = "사용자의 예약 취소 (결제 전 예약건에 대해)")
    public Response<UserReservationPatchCancelResponse> cancelReservation(@ApiIgnore Authentication authentication,
                                                                          @PathVariable Long reservationId) {

        UserReservationPatchCancelResponse cancelResponse = userReservationService.cancelReservation(
                authentication.getName(), reservationId);

        return Response.success(cancelResponse);
    }
}
