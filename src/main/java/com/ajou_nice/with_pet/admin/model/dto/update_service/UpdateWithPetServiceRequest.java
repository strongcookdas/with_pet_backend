package com.ajou_nice.with_pet.admin.model.dto.update_service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UpdateWithPetServiceRequest {
    @NotNull
    private String serviceName;
    @NotNull
    private String serviceImg;
    @NotNull
    private String serviceIntroduction;
}
