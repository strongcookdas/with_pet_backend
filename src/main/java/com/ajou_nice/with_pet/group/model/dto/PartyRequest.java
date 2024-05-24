package com.ajou_nice.with_pet.group.model.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
public class PartyRequest {

    @NotBlank
    private String partyName;
    private String dog_img;
    @NotBlank
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
