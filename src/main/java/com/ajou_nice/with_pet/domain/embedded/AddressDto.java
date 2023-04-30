package com.ajou_nice.with_pet.domain.embedded;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AddressDto {

    private String zipcode;
    private String street_adr;
    private String detail_adr;
}
