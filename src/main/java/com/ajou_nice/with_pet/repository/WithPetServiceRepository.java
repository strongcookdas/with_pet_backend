package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Service;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithPetServiceRepository extends JpaRepository<Service, Long> {

	Optional <Service> findByName(String name);
}
