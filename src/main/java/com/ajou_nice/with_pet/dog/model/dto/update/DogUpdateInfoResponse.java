package com.ajou_nice.with_pet.dog.model.dto.update;

import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DogUpdateInfoResponse {
    private Long dogId;
    private String dogImg;
    private String dogName;
    private String dogBreed;
    private Gender dogGender;
    private Boolean dogNeutralization;
    private LocalDate dogBirth;
    private Float dogWeight;
    private String dogIsbn;
    private Double dogSocializationTemperature;
    private Integer dogSocializationDegree;
    private Double dogAffectionTemperature;
    private DogSize dogSize;

    public static DogUpdateInfoResponse of(Dog dog) {

        return DogUpdateInfoResponse.builder()
                .dogId(dog.getDogId())
                .dogImg(dog.getDogProfileImg())
                .dogName(dog.getDogName())
                .dogBreed(dog.getDogBreed())
                .dogGender(dog.getDogGender())
                .dogNeutralization(dog.getDogNeutralization())
                .dogBirth(dog.getDogBirth())
                .dogWeight(dog.getDogWeight())
                .dogIsbn(dog.getDogIsbn())
                .dogSocializationTemperature(dog.getDogSocializationTemperature())
                .dogSocializationDegree(dog.getDogSocializationDegree())
                .dogAffectionTemperature(dog.getDogAffectionTemperature())
                .dogSize(dog.getDogSize())
                .build();
    }
}
