package com.ajou_nice.with_pet.domain.entity.embedded;

import com.ajou_nice.with_pet.domain.embedded.AddressDto;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Address {
    private String zipcode;
    private String street_adr;
    private String detail_adr;

    public static Address of(AddressDto addressDto) {
        return Address.builder()
                .zipcode(addressDto.getZipcode())
                .street_adr(addressDto.getStreet_adr())
                .detail_adr(addressDto.getDetail_adr())
                .build();
    }
}
