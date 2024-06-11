package com.ajou_nice.with_pet.reservation.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReservationCreateRequest {

    @NotNull(message = "체크인 날짜를 선택해주세요.")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime reservationCheckIn;

    @NotNull(message = "체크아웃 날짜를 선택해주세요.")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime reservationCheckOut;

    List<Long> reservationOptionIdList;

    @NotNull(message = "반려견을 선택해주세요.")
    private Long dogId;

    @NotNull
    private Long petSitterId;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ReservationSimpleRequest{
        private Long reservationId;
    }

}
