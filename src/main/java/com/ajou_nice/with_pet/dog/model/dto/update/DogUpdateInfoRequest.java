package com.ajou_nice.with_pet.dog.model.dto.update;

import com.ajou_nice.with_pet.enums.Gender;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class DogUpdateInfoRequest {

    private String dogImg;
    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$", message = "올바른 이름을 입력하세요.")
    private String dogName;
    @NotBlank
    private String dogBreed;
    @NotBlank
    private Gender dogGender;
    @NotNull
    private Boolean dogNeutralization;
    @NotBlank
    private LocalDate dogBirth;
    @NotBlank
    private Float dogWeight;
}
