package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.service.PetSitterService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetSitterServiceRepository extends JpaRepository<PetSitterWithPetService, Long> {


	@Query("select s from PetSitterWithPetService s where s.petSitter.id=:petSitterId")
	Optional<List<PetSitterService>> findAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);

	@Query("delete from PetSitterWithPetService p where p.petSitter.id=:petSitterId")
	void deleteAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);
}
