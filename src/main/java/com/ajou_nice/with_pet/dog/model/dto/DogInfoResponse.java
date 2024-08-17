package com.ajou_nice.with_pet.dog.model.dto;

import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.enums.DogSize;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.ajou_nice.with_pet.enums.Gender;
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
public class DogInfoResponse {

    private Long dogId;
    private String dogImg;
    private String dogName;
    private String dogBreed;
    private Gender dogGender;
    private Boolean neutralization;
    private LocalDate dogBirth;
    private Float dogWeight;
    private String dogIsbn;
    private Double socializationTemperature;
    private Integer socializationDegree;
    private Double affectionTemperature;
    private DogSize dogSize;


    public static List<DogInfoResponse> toList(List<Dog> dogs) {
        return dogs.stream().map(dog -> DogInfoResponse.builder()
                .dogId(dog.getDogId())
                .dogImg(dog.getDogProfileImg())
                .dogName(dog.getDogName())
                .dogBreed(dog.getDogBreed())
                .dogGender(dog.getDogGender())
                .neutralization(dog.getDogNeutralization())
                .dogBirth(dog.getDogBirth())
                .dogWeight(dog.getDogWeight())
                .dogIsbn(dog.getDogIsbn())
                .socializationTemperature(dog.getDogSocializationTemperature())
                .socializationTemperature(dog.getDogSocializationTemperature())
                .affectionTemperature(dog.getDogAffectionTemperature())
                .dogSize(dog.getDogSize())
                .build()).collect(Collectors.toList());
    }

    public static DogInfoResponse of(Dog dog) {

        return DogInfoResponse.builder()
                .dogId(dog.getDogId())
                .dogImg(dog.getDogProfileImg())
                .dogName(dog.getDogName())
                .dogBreed(dog.getDogBreed())
                .dogGender(dog.getDogGender())
                .neutralization(dog.getDogNeutralization())
                .dogBirth(dog.getDogBirth())
                .dogWeight(dog.getDogWeight())
                .dogIsbn(dog.getDogIsbn())
                .socializationTemperature(dog.getDogSocializationTemperature())
                .socializationDegree(dog.getDogSocializationDegree())
                .affectionTemperature(dog.getDogAffectionTemperature())
                .dogSize(dog.getDogSize())
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return this.dogId == obj;
    }
}
