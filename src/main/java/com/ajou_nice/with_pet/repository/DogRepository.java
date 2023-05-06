package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {

}
