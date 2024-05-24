package com.ajou_nice.with_pet.dog.model.dto;

import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.enums.DogSize;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
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
    private String dog_gender;
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
                .dog_img(dog.getProfile_img())
                .dog_name(dog.getName())
                .dog_breed(dog.getBreed())
                .dog_gender(dog.getGender())
                .neutralization(dog.getNeutralization())
                .dog_birth(dog.getBirth())
                .dog_weight(dog.getWeight())
                .dog_isbn(dog.getIsbn())
                .socializationTemperature(dog.getSocializationTemperature())
                .socializationTemperature(dog.getSocializationTemperature())
                .affectionTemperature(dog.getAffectionTemperature())
                .dogSize(dog.getDogSize())
                .build()).collect(Collectors.toList());
    }

    public static DogInfoResponse of(Dog dog) {

        return DogInfoResponse.builder()
                .dog_id(dog.getDogId())
                .dog_img(dog.getProfile_img())
                .dog_name(dog.getName())
                .dog_breed(dog.getBreed())
                .dog_gender(dog.getGender())
                .neutralization(dog.getNeutralization())
                .dog_birth(dog.getBirth())
                .dog_weight(dog.getWeight())
                .dog_isbn(dog.getIsbn())
                .socializationTemperature(dog.getSocializationTemperature())
                .socializationDegree(dog.getSocializationDegree())
                .affectionTemperature(dog.getAffectionTemperature())
                .dogSize(dog.getDogSize())
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return this.dog_id == obj;
    }
}
