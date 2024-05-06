package com.ajou_nice.with_pet.withpet_service.repository;

import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithPetServiceRepository extends JpaRepository<WithPetService, Long> {

	Optional <WithPetService> findByName(String name);
}
