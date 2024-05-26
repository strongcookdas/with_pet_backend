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

    private Long dog_id;
    private String dog_img;
    private String dog_name;
    private String dog_breed;
    private Gender dog_gender;
    private Boolean neutralization;
    private LocalDate dog_birth;
    private Float dog_weight;
    private String dog_isbn;
    private Double socializationTemperature;
    private Integer socializationDegree;
    private Double affectionTemperature;
    private DogSize dogSize;


    public static List<DogInfoResponse> toList(List<Dog> dogs) {
        return dogs.stream().map(dog -> DogInfoResponse.builder()
                .dog_id(dog.getDogId())
                .dog_img(dog.getDogProfileImg())
                .dog_name(dog.getDogName())
                .dog_breed(dog.getDogBreed())
                .dog_gender(dog.getDogGender())
                .neutralization(dog.getDogNeutralization())
                .dog_birth(dog.getDogBirth())
                .dog_weight(dog.getDogWeight())
                .dog_isbn(dog.getDogIsbn())
                .socializationTemperature(dog.getDogSocializationTemperature())
                .socializationTemperature(dog.getDogSocializationTemperature())
                .affectionTemperature(dog.getDogAffectionTemperature())
                .dogSize(dog.getDogSize())
                .build()).collect(Collectors.toList());
    }

    public static DogInfoResponse of(Dog dog) {

        return DogInfoResponse.builder()
                .dog_id(dog.getDogId())
                .dog_img(dog.getDogProfileImg())
                .dog_name(dog.getDogName())
                .dog_breed(dog.getDogBreed())
                .dog_gender(dog.getDogGender())
                .neutralization(dog.getDogNeutralization())
                .dog_birth(dog.getDogBirth())
                .dog_weight(dog.getDogWeight())
                .dog_isbn(dog.getDogIsbn())
                .socializationTemperature(dog.getDogSocializationTemperature())
                .socializationDegree(dog.getDogSocializationDegree())
                .affectionTemperature(dog.getDogAffectionTemperature())
                .dogSize(dog.getDogSize())
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return this.dog_id == obj;
    }
}
