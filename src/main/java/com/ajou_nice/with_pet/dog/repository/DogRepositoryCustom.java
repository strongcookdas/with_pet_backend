package com.ajou_nice.with_pet.dog.repository;

import com.ajou_nice.with_pet.dog.model.entity.Dog;
import java.util.List;

public interface DogRepositoryCustom {

    List<Dog> findAllByUserParty(String userId);

}
