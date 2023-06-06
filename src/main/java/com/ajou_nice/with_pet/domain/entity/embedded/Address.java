package com.ajou_nice.with_pet.domain.entity.embedded;

import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Address {

    private String zipcode;
    private String streetAdr;
    private String detailAdr;



    public static Address toAddressEntity(AddressDto addressDto) {
        return Address.builder()
                .zipcode(addressDto.getZipcode())
                .streetAdr(addressDto.getStreetAdr())
                .detailAdr(addressDto.getDetailAdr())
                .build();
    }

    public static Address simpleAddressGenerator(String zipcode, String streetAdr, String detailAdr){
        return Address.builder()
                .zipcode(zipcode)
                .streetAdr(streetAdr)
                .detailAdr(detailAdr)
                .build();
    }
}
