package com.ajou_nice.with_pet.domain.dto.reservation;

import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterCriticalServiceRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ReservationRequest {

    @NotNull(message = "체크인 날짜를 선택해주세요.")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime checkIn;

    @NotNull(message = "체크아웃 날짜를 선택해주세요.")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime checkOut;

    List<Long> optionId;

    @NotNull(message = "반려견을 선택해주세요.")
    private Long dogId;

    @NotNull
    private Long petsitterId;

    public static ReservationRequest forSimpleTest(LocalDateTime checkIn, LocalDateTime checkOut,
            Long dogId, Long petSitterId){
        return ReservationRequest.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .petsitterId(petSitterId)
                .dogId(dogId)
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ReservationSimpleRequest{
        private Long reservationId;
    }

}
