package com.ajou_nice.with_pet.fixture.entity;

import com.ajou_nice.with_pet.domain.entity.embedded.Address;

public class AddressFixture {
    public static Address createAddress(String zipcode, String streetAdr, String detailAdr) {
        return Address.builder()
                .zipcode(zipcode)
                .streetAdr(streetAdr)
                .detailAdr(detailAdr)
                .build();
    }
}
