package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetSitterServiceRepository extends JpaRepository<PetSitterWithPetService, Long> {

}
