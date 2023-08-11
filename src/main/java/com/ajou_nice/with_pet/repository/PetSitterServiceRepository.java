package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PetSitterServiceRepository extends JpaRepository<PetSitterWithPetService, Long> {


	@Query("select s from PetSitterWithPetService s where s.petSitter.petSitterId=:petSitterId")
	List<PetSitterWithPetService> findAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);

	@Modifying
	@Transactional
	@Query("delete from PetSitterWithPetService p where p.petSitter.petSitterId=:petSitterId")
	void deleteAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);
}
