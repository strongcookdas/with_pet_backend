package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.repository.custom.DogRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long>, DogRepositoryCustom {

    List<Dog> findAllByParty(Party party);
}
