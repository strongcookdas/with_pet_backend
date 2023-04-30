package com.ajou_nice.with_pet.domain.entity.embedded;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String zipcode;
    private String street_adr;
    private String detail_adr;
}
