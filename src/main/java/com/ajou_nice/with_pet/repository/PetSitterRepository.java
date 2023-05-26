package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.repository.custom.PetSitterRespositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetSitterRepository extends JpaRepository<PetSitter, Long> ,
		PetSitterRespositoryCustom {

	//쿼리 dsl 사용 필요
	Optional<PetSitter> findByUser(User user);
}
