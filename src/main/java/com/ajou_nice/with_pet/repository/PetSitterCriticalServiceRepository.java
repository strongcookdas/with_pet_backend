package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetSitterCriticalServiceRepository extends JpaRepository<PetSitterCriticalService, Long> {

}
