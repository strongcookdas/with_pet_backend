package com.ajou_nice.with_pet.domain.dto.embedded;

import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AddressDto {

    @NotBlank
    private String zipcode;
    @NotBlank
    private String streetAdr;
    @NotBlank
    private String detailAdr;

    public static AddressDto of(Address address) {
        return AddressDto.builder()
                .zipcode(address.getZipcode())
                .streetAdr(address.getStreetAdr())
                .detailAdr(address.getDetailAdr())
                .build();
    }
}
