package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetSitterApplicantRepository extends JpaRepository<PetSitterApplicant, Long> {
	Optional<PetSitterApplicant> findById(Long id);

	Optional<PetSitterApplicant> findByUser(User user);

}
