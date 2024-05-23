package com.ajou_nice.with_pet.petsitter.repository;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.petsitter.repository.custom.PetSitterRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetSitterRepository extends JpaRepository<PetSitter, Long> ,
        PetSitterRepositoryCustom {

	//쿼리 dsl 사용 필요
	Optional<PetSitter> findByUser(User user);
	Optional<PetSitter> findByUserEmail(String email);
}
