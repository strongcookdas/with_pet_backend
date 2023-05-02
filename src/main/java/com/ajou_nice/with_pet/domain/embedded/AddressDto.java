package com.ajou_nice.with_pet.domain.embedded;

import com.ajou_nice.with_pet.domain.entity.embedded.Address;
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

    public Address toAddressEntity() {
        return Address.builder()
                .zipcode(this.getZipcode())
                .street_adr(this.getStreet_adr())
                .detail_adr(this.getDetail_adr())
                .build();
    }
}
