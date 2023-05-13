package com.ajou_nice.with_pet.repository.custom;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DogRepositoryCustom {

    Page<Dog> findAllByUserParty(Pageable pageable, String userId);

    List<Dog> findAllByUserParty(String userId);

}
