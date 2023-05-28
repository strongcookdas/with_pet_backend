package com.ajou_nice.with_pet.repository.custom;

import com.ajou_nice.with_pet.domain.entity.Dog;
import java.util.List;

public interface DogRepositoryCustom {

    List<Dog> findAllByUserParty(String userId);

}
