package com.ajou_nice.with_pet.domain.embedded;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AddressDto {

    @NotBlank
    private String zipcode;
    @NotBlank
    private String street_adr;
    @NotBlank
    private String detail_adr;
}
