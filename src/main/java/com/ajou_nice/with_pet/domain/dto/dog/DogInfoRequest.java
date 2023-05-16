package com.ajou_nice.with_pet.domain.dto.dog;

import com.ajou_nice.with_pet.enums.DogSize;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class DogInfoRequest {

    private String dog_img;
    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$", message = "올바른 이름을 입력하세요.")
    private String dog_name;
    @NotBlank
    private String dog_breed;
    @NotBlank
    private String dog_gender;
    @NotBlank
    private Boolean neutralization;
    @NotBlank
    private LocalDate dog_birth;
    @NotBlank
    private Float dog_weight;
    @NotBlank
    private String dog_isbn;
}
