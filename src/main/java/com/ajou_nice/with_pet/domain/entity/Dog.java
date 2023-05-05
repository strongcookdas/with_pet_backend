package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.dog.DogInfoRequest;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
public class Dog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dogId;
    @NotNull
    private String name;
    @NotNull
    private String gender;
    @NotNull
    private Boolean neutralization;
    @NotNull
    private LocalDate birth;
    @NotNull
    private Float weight;
    private String profile_img;
    @NotNull
    private String breed;

    private String isbn;

    public void update(DogInfoRequest dogInfoRequest) {
        this.name = dogInfoRequest.getDog_name();
        this.gender = dogInfoRequest.getDog_gender();
        this.neutralization = dogInfoRequest.getNeutralization();
        this.birth = dogInfoRequest.getDog_birth();
        this.weight = dogInfoRequest.getDog_weight();
        this.profile_img = dogInfoRequest.getDog_img();
        this.breed = dogInfoRequest.getDog_breed();
        this.isbn = dogInfoRequest.getDog_isbn();
    }

    public static Dog of(DogInfoRequest dogInfoRequest) {
        return Dog.builder()
                .name(dogInfoRequest.getDog_name())
                .gender(dogInfoRequest.getDog_gender())
                .neutralization(dogInfoRequest.getNeutralization())
                .birth(dogInfoRequest.getDog_birth())
                .weight(dogInfoRequest.getDog_weight())
                .profile_img(dogInfoRequest.getDog_img())
                .breed(dogInfoRequest.getDog_breed())
                .isbn(dogInfoRequest.getDog_isbn())
                .build();
    }
}
