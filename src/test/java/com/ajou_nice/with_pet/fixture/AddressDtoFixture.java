package com.ajou_nice.with_pet.fixture;

import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;

public class AddressDtoFixture {
    public static AddressDto createAddressDto(String zipcode, String streetAdr, String detailAdr) {
        return AddressDto.builder()
                .zipcode(zipcode)
                .streetAdr(streetAdr)
                .detailAdr(detailAdr)
                .build();
    }
}
