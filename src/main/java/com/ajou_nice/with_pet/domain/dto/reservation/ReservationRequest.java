package com.ajou_nice.with_pet.domain.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReservationRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @NotNull(message = "체크인 날짜를 선택해주세요.")
    private LocalDateTime checkIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @NotNull(message = "체크아웃 날짜를 선택해주세요.")
    private LocalDateTime checkOut;

    @NotNull
    List<Long> optionId;

    @NotNull(message = "반려견을 선택해주세요.")
    private Long dogId;

    @NotNull
    private Long petsitterId;

}
