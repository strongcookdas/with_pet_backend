package com.ajou_nice.with_pet.dto.house;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class HouseRequest {

    @NotNull(message = "이미지를 업로드해주세요.")
    private String houseImg;
    @NotNull(message = "대표사진 여부값을 입력해주세요.")
    private Boolean isRepresentative;
}
