package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterHashTag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetSitterHashTagRepository extends JpaRepository<PetSitterHashTag, Long> {

	@Query("select t from PetSitterHashTag t where t.petSitter.id=:petSitterId")
	Optional<List<PetSitterHashTag>> findAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);

	@Query("delete from PetSitterHashTag t where t.petSitter.id=:petSitterId")
	void deleteAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);
}
