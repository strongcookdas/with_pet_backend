package com.ajou_nice.with_pet.fixture;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.applicant.model.dto.PetsitterApplicationRequest;
import com.ajou_nice.with_pet.applicant.model.dto.PetsitterApplicationResponse;
import com.ajou_nice.with_pet.enums.Gender;

import java.time.LocalDate;

public class PetsitterApplicationDtoFixture {
    public static PetsitterApplicationRequest createPetsitterApplicationRequest(LocalDate birth, Boolean isSmoking, Gender gender, Boolean havingWithPet, String animalCareer, String motivation, String licenseImg) {
        return PetsitterApplicationRequest.builder()
                .birth(birth)
                .isSmoking(isSmoking)
                .gender(gender)
                .havingWithPet(havingWithPet)
                .animalCareer(animalCareer)
                .motivation(motivation)
                .licenseImg(licenseImg)
                .build();
    }

    public static PetsitterApplicationResponse createPetsitterApplicationResponse(User user){
        return PetsitterApplicationResponse.of(user);
    }
}
