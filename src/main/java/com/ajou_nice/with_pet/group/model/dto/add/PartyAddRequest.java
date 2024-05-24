package com.ajou_nice.with_pet.group.model.dto.add;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;

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
public class PartyAddRequest {

    @NotBlank
    private String partyName;
    private String partyDogImg;
    @NotBlank
    private String partyDogName;
    @NotBlank
    private String partyDogBreed;
    @NotBlank
    private String partyDogGender;
    @NotBlank
    private Boolean partyDogNeutralization;
    @NotBlank
    private LocalDate partyDogBirth;
    @NotBlank
    private Float partyDogWeight;
    @NotBlank
    private String partyDogIsbn;
}
