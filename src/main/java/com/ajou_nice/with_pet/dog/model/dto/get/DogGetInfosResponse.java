package com.ajou_nice.with_pet.dog.model.dto.get;

import com.ajou_nice.with_pet.dog.model.dto.add.DogRegisterResponse;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.enums.DogSize;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DogGetInfosResponse {
    private Long dogId;
    private String dogImg;
    private String dogName;
    private String dogBreed;
    private String dogGender;
    private Boolean dogNeutralization;
    private LocalDate dogBirth;
    private Float dogWeight;
    private String dogIsbn;
    private Double dogSocializationTemperature;
    private Integer dogSocializationDegree;
    private Double dogAffectionTemperature;
    private DogSize dogSize;

    public static DogGetInfosResponse of(Dog dog) {

        return DogGetInfosResponse.builder()
                .dogId(dog.getDogId())
                .dogImg(dog.getProfile_img())
                .dogName(dog.getName())
                .dogBreed(dog.getBreed())
                .dogGender(dog.getGender())
                .dogNeutralization(dog.getNeutralization())
                .dogBirth(dog.getBirth())
                .dogWeight(dog.getWeight())
                .dogIsbn(dog.getIsbn())
                .dogSocializationTemperature(dog.getSocializationTemperature())
                .dogSocializationDegree(dog.getSocializationDegree())
                .dogAffectionTemperature(dog.getAffectionTemperature())
                .dogSize(dog.getDogSize())
                .build();
    }
}
